package bg.credihub.payment.service;

import bg.credihub.payment.exceptions.LoanAlreadyExistsException;
import bg.credihub.payment.mapper.LoanAccountMapper;
import bg.credihub.payment.models.dtos.LoanAccountRequest;
import bg.credihub.payment.models.dtos.LoanAccountResponse;
import bg.credihub.payment.models.entities.Installment;
import bg.credihub.payment.models.entities.LoanAccount;
import bg.credihub.payment.models.enums.InstallmentStatus;
import bg.credihub.payment.models.enums.LoanStatus;
import bg.credihub.payment.repository.InstallmentRepository;
import bg.credihub.payment.repository.LoanAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoanAccountService {
    private final LoanAccountRepository loanAccountRepository;
    private final InstallmentRepository installmentRepository;
    private final LoanAccountMapper loanAccountMapper;

    public LoanAccountService(LoanAccountRepository loanAccountRepository, InstallmentRepository installmentRepository, LoanAccountMapper loanAccountMapper) {
        this.loanAccountRepository = loanAccountRepository;
        this.installmentRepository = installmentRepository;
        this.loanAccountMapper = loanAccountMapper;
    }

    @Transactional
    public LoanAccountResponse createLoan(LoanAccountRequest request) {
        validateRequest(request);

        LoanAccount loanAccount = createLoanAccount(request);

        loanAccountRepository.save(loanAccount);

        List<Installment> installments = generateInstallments(loanAccount);

        installmentRepository.saveAll(installments);

        return LoanAccountResponse.builder().id(loanAccount.getId()).build();
    }

    private LoanAccount createLoanAccount(LoanAccountRequest request) {
        return LoanAccount.builder()
                .applicationId(request.getApplicationId())
                .userId(request.getUserId())
                .principalAmount(request.getPrincipalAmount())
                .remainingBalance(request.getPrincipalAmount())
                .annualInterestRate(request.getAnnualInterestRate())
                .monthlyPayment(request.getMonthlyPayment())
                .periodMonths(request.getPeriodMonths())
                .paidInstallments(0)
                .status(LoanStatus.ACTIVE)
                .startDate(request.getStartDate())
                .endDate(request.getStartDate().plusMonths(request.getPeriodMonths()))
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

    public List<LoanAccountResponse> getUserLoans(UUID userId) {
        return loanAccountRepository.findByUserId(userId)
                .stream()
                .map(loanAccountMapper::toLoanAccountResponse)
                .toList();
    }

    private void validateRequest(LoanAccountRequest request) {
        if (loanAccountRepository.existsByApplicationId(request.getApplicationId())) {
            throw new LoanAlreadyExistsException("Loan already exists for this application.");
        }
    }
}
