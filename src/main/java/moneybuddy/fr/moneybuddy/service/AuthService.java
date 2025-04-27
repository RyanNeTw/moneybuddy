package moneybuddy.fr.moneybuddy.service;

import moneybuddy.fr.moneybuddy.dtos.AuthRequest;
import moneybuddy.fr.moneybuddy.dtos.AuthResponse;
import moneybuddy.fr.moneybuddy.dtos.AuthSubAccountRequest;
import moneybuddy.fr.moneybuddy.dtos.RegisterRequest;
import moneybuddy.fr.moneybuddy.model.Account;
import moneybuddy.fr.moneybuddy.model.PlanType;
import moneybuddy.fr.moneybuddy.model.Role;
import moneybuddy.fr.moneybuddy.model.SubAccount;
import moneybuddy.fr.moneybuddy.model.SubAccountRole;
import moneybuddy.fr.moneybuddy.repository.AccountRepository;
import moneybuddy.fr.moneybuddy.repository.SubAccountRepository;
import moneybuddy.fr.moneybuddy.utils.CheckByRegex;
import moneybuddy.fr.moneybuddy.utils.CreateDefaultSubAccounts;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository repository;
    private final SubAccountRepository subAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CheckByRegex checkByRegex;
    private final CreateDefaultSubAccounts createDefaultSubAccounts;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PIN_REGEX = "^\\d{4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    public ResponseEntity<AuthResponse> response(String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(AuthResponse.builder()
                    .error(message)
                    .build());
    }

    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
        if (!checkByRegex.validate(request.getEmail(), EMAIL_REGEX)) {
            return response("Mauvais format d'email" ,HttpStatus.BAD_REQUEST);
        }

        if (!checkByRegex.validate(request.getPassword(), PASSWORD_REGEX)) {
            return response("Mauvais format de mot de passe" ,HttpStatus.BAD_REQUEST);
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return response("Les mots de passe ne correspondent pas" ,HttpStatus.BAD_REQUEST);
        }
        
        if (repository.findByEmail(request.getEmail()).isPresent()){
            return response("L'email est déjà utilisé" ,HttpStatus.BAD_REQUEST);
        }

        if (!checkByRegex.validate(request.getPin(), PIN_REGEX)) {
            return response("Pin doit avoir 4 chiffres" ,HttpStatus.BAD_REQUEST);
        }

        var account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .pin(request.getPin())
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .subscriptionStatus(false)
                .planType(PlanType.FREE)
                .build();

        repository.save(account);

        List<SubAccount> subAccounts = createDefaultSubAccounts.createDefaultSubAccounts();
        for (SubAccount subAccount : subAccounts) {
            subAccount.setUserId(account.getId());
        }

        subAccountRepository.saveAll(subAccounts);

        account.setSubAccounts(subAccounts);
        repository.save(account);
        
        var jwtToken = jwtService.generateToken(account);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(AuthResponse.builder()
                .token(jwtToken)
                .build());
    }

    public ResponseEntity<AuthResponse> authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        var account = repository.findByEmail(request.getEmail())
        .orElseThrow();

        var jwtToken = jwtService.generateToken(account);

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(AuthResponse.builder()
                .token(jwtToken)
                .build());
    }

    public ResponseEntity<Account> getMe(String token) {
        String email = jwtService.extractUsername(token);
        Account user = repository.findByEmail(email).orElseThrow();

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(user);
    }

    public ResponseEntity<AuthResponse> authenticateSubAccount(AuthSubAccountRequest request, String token) {
        String id = request.getId();
        String pin = request.getPin();
        String email = jwtService.extractUsername(token);

        SubAccount subAccount = subAccountRepository.findById(id).orElseThrow();
        Account account = repository.findByEmail(email).orElseThrow();

        if (SubAccountRole.PARENT.equals(subAccount.getRole()) && !pin.equals(account.getPin())) {
            return response("Mauvais pin pour le compte parent", HttpStatus.BAD_REQUEST);
        }        

        var jwtToken = jwtService.generateSubAccountToken(id, email, subAccount.getRole());
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(AuthResponse.builder()
                .token(jwtToken)
                .build());
    }
}