package bg.credihub.payment.models.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoanRequest {
    private UUID applicationId;
    private UUID userId;
    private BigDecimal principalAmount;
    private BigDecimal annualInterestRate;
    private BigDecimal monthlyPayment;
    private Integer periodMonths;
    private LocalDate startDate;
}
