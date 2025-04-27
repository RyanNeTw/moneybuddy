package moneybuddy.fr.moneybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subAccounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubAccount {
    @Id
    private String id;
    private String userId;
    private String name;
    private SubAccountRole role;
    private LocalDateTime createdAt;
    private boolean isActive;
}