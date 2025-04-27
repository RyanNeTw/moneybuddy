package moneybuddy.fr.moneybuddy.utils;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import moneybuddy.fr.moneybuddy.model.SubAccount;
import moneybuddy.fr.moneybuddy.model.SubAccountRole;

@Component
public class CreateDefaultSubAccounts {
    
    public List<SubAccount> createDefaultSubAccounts() {
        return List.of(
            SubAccount.builder()
                .name("Parent 1")
                .role(SubAccountRole.PARENT)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build(),
            
            SubAccount.builder()
                .name("Parent 2")
                .role(SubAccountRole.PARENT)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build(),
            
            SubAccount.builder()
                .name("Child 1")
                .role(SubAccountRole.CHILD)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build(),
            
            SubAccount.builder()
                .name("Child 2")
                .role(SubAccountRole.CHILD)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build()
        );
    }
}