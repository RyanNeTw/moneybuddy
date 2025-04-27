package moneybuddy.fr.moneybuddy.controller;

import moneybuddy.fr.moneybuddy.dtos.SubAccountDto;
import moneybuddy.fr.moneybuddy.model.SubAccount;
import moneybuddy.fr.moneybuddy.service.SubAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-accounts")
@RequiredArgsConstructor
public class SubAccountController {
    private final SubAccountService subAccountService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<SubAccount>> getSubAccounts() {
        return ResponseEntity.ok(subAccountService.getSubAccounts());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> addSubAccount(
            @RequestBody SubAccountDto subAccountDto,
            @RequestParam String pin
    ) {
        subAccountService.addSubAccount(subAccountDto, pin);
        return ResponseEntity.ok().build();
    }
}