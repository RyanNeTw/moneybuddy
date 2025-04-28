package moneybuddy.fr.moneybuddy.controller;

import moneybuddy.fr.moneybuddy.dtos.AuthRequest;
import moneybuddy.fr.moneybuddy.dtos.AuthResponse;
import moneybuddy.fr.moneybuddy.dtos.AuthSubAccountRequest;
import moneybuddy.fr.moneybuddy.dtos.RegisterRequest;
import moneybuddy.fr.moneybuddy.model.Account;
import moneybuddy.fr.moneybuddy.model.SubAccount;
import moneybuddy.fr.moneybuddy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
        return service.authenticate(request);
    }

    @GetMapping("/me")
    public ResponseEntity<Account> getMe(
      @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        return service.getMe(token);
    }

    @PostMapping("/subAccount/login")
    public ResponseEntity<AuthResponse> authenticateSubAccount(
        @RequestBody AuthSubAccountRequest request,
        @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        return service.authenticateSubAccount(request, token);
    }

    @GetMapping("/subAccount/me")
    public ResponseEntity<SubAccount> getSubAccountMe(
        @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        return service.getSubAccountMe(token);
    }
}