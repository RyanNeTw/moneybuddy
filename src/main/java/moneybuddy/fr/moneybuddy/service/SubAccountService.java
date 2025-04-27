package moneybuddy.fr.moneybuddy.service;

import moneybuddy.fr.moneybuddy.dtos.SubAccountDto;
import moneybuddy.fr.moneybuddy.model.Account;
import moneybuddy.fr.moneybuddy.model.SubAccount;
import moneybuddy.fr.moneybuddy.model.SubAccountRole;
import moneybuddy.fr.moneybuddy.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubAccountService {
    private final AccountRepository accountRepository;

    public List<SubAccount> getSubAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        return account.getSubAccounts();
    }

    public void addSubAccount(SubAccountDto subAccountDto, String pin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        if (!account.getPin().equals(pin)) {
            throw new RuntimeException("Invalid PIN");
        }
        
        SubAccount subAccount = SubAccount.builder()
                .name(subAccountDto.getName())
                .role(subAccountDto.getRole())
                .build();
        
        account.getSubAccounts().add(subAccount);
        accountRepository.save(account);
    }
}