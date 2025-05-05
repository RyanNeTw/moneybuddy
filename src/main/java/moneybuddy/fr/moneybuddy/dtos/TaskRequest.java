package moneybuddy.fr.moneybuddy.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    @NotBlank(message = "La category est obligatoire")
    private String category;

    @NotBlank(message = "Le subAccountId est obligatoire")
    private String subAccountId;

    @NotBlank(message = "Le reward est obligatoire")
    private String reward;

    @NotBlank(message = "Le dateLimit est obligatoire")
    private String dateLimit;
}