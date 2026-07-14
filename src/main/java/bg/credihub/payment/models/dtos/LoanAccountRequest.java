package bg.credihub.payment.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanAccountRequest {
    @NotNull
    private UUID applicationId;
    @NotNull
    private UUID userId;
    @NotNull
    @Positive
    private BigDecimal principalAmount;
    @NotNull
    @Positive
    private BigDecimal annualInterestRate;
    @NotNull
    private BigDecimal monthlyPayment;
    @NotNull
    @Min(1)
    private Integer periodMonths;
    @NotNull
    private LocalDate startDate;
}
