package moneybuddy.fr.moneybuddy.service;

import moneybuddy.fr.moneybuddy.dtos.AuthRequest;
import moneybuddy.fr.moneybuddy.dtos.AuthResponse;
import moneybuddy.fr.moneybuddy.dtos.RegisterRequest;
import moneybuddy.fr.moneybuddy.model.Account;
import moneybuddy.fr.moneybuddy.model.Role;
import moneybuddy.fr.moneybuddy.repository.AccountRepository;
import moneybuddy.fr.moneybuddy.utils.CheckByRegex;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CheckByRegex checkByRegex;

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
                .build();
        
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
}