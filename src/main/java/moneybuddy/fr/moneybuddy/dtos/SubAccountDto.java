package moneybuddy.fr.moneybuddy.dtos;

import moneybuddy.fr.moneybuddy.model.SubAccountRole;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubAccountDto {
    private String name;
    private SubAccountRole role;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now(); 
}