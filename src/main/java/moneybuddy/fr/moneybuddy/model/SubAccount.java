package moneybuddy.fr.moneybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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
    private String accountId;
    private String name;
    private SubAccountRole role;
    private Date createdAt;
    private boolean isActive;
}