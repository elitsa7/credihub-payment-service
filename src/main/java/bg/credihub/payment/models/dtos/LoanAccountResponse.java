package bg.credihub.payment.models.dtos;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class LoanAccountResponse {
    private UUID id;
    private BigDecimal principalAmount;
    private BigDecimal remainingBalance;
    private BigDecimal monthlyPayment;
    private Integer paidInstallments;
    private Integer periodMonths;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
}
