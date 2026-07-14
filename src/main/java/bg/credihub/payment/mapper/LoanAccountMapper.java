package bg.credihub.payment.mapper;

import bg.credihub.payment.models.dtos.LoanAccountResponse;
import bg.credihub.payment.models.entities.LoanAccount;
import org.springframework.stereotype.Component;

@Component
public class LoanAccountMapper {
    public LoanAccountResponse toLoanAccountResponse(LoanAccount loanAccount) {

        return LoanAccountResponse.builder()
                .id(loanAccount.getId())
                .principalAmount(loanAccount.getPrincipalAmount())
                .remainingBalance(loanAccount.getRemainingBalance())
                .monthlyPayment(loanAccount.getMonthlyPayment())
                .paidInstallments(loanAccount.getPaidInstallments())
                .periodMonths(loanAccount.getPeriodMonths())
                .status(loanAccount.getStatus().name())
                .startDate(loanAccount.getStartDate())
                .endDate(loanAccount.getEndDate())
                .build();
    }
}
