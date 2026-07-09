package bg.credihub.payment.service;

import bg.credihub.payment.exceptions.LoanAlreadyExistsException;
import bg.credihub.payment.models.dto.CreateLoanRequest;
import bg.credihub.payment.models.entity.Installment;
import bg.credihub.payment.models.entity.LoanAccount;
import bg.credihub.payment.models.enums.InstallmentStatus;
import bg.credihub.payment.models.enums.LoanStatus;
import bg.credihub.payment.repository.InstallmentRepository;
import bg.credihub.payment.repository.LoanAccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanAccountService {
    private final LoanAccountRepository loanAccountRepository;
    private final InstallmentRepository installmentRepository;

    public LoanAccountService(LoanAccountRepository loanAccountRepository, InstallmentRepository installmentRepository) {
        this.loanAccountRepository = loanAccountRepository;
        this.installmentRepository = installmentRepository;
    }

    public void createLoan(CreateLoanRequest request) {
        validateRequest(request);

        LoanAccount loanAccount = createLoanAccount(request);

        loanAccountRepository.save(loanAccount);

        List<Installment> installments = generateInstallments(loanAccount);

        installmentRepository.saveAll(installments);
    }

    private LoanAccount createLoanAccount(CreateLoanRequest request) {
        return LoanAccount.builder()
                .applicationId(request.getApplicationId())
                .userId(request.getUserId())
                .principalAmount(request.getPrincipalAmount())
                .remainingPrincipal(request.getPrincipalAmount())
                .annualInterestRate(request.getAnnualInterestRate())
                .monthlyPayment(request.getMonthlyPayment())
                .periodMonths(request.getPeriodMonths())
                .paidInstallments(0)
                .status(LoanStatus.ACTIVE)
                .startDate(request.getStartDate())
                .build();
    }

    private List<Installment> generateInstallments(LoanAccount loanAccount) {
        List<Installment> installments = new ArrayList<>();

        LocalDate dueDate = loanAccount.getStartDate().plusMonths(1);

        for (int i = 1; i <= loanAccount.getPeriodMonths(); i++) {
            Installment installment = Installment.builder()
                    .loanAccount(loanAccount)
                    .installmentNumber(i)
                    .amount(loanAccount.getMonthlyPayment())
                    .dueDate(dueDate)
                    .status(InstallmentStatus.PENDING)
                    .build();

            installments.add(installment);
            dueDate = dueDate.plusMonths(1);
        }
        return installments;
    }

    private void validateRequest(CreateLoanRequest request) {
        if (loanAccountRepository.existsByApplicationId(request.getApplicationId())) {
            throw new LoanAlreadyExistsException("Loan already exists for this application.");
        }
    }
}
