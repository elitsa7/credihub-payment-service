package bg.credihub.payment.service;

import bg.credihub.payment.mapper.InstallmentMapper;
import bg.credihub.payment.models.dtos.InstallmentResponse;
import bg.credihub.payment.models.entities.Installment;
import bg.credihub.payment.models.enums.InstallmentStatus;
import bg.credihub.payment.repository.InstallmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InstallmentService {
    private final InstallmentRepository installmentRepository;
    private final InstallmentMapper installmentMapper;

    public InstallmentService(InstallmentRepository installmentRepository, InstallmentMapper installmentMapper) {
        this.installmentRepository = installmentRepository;
        this.installmentMapper = installmentMapper;
    }

    public List<InstallmentResponse> getLoanInstallments(UUID loanAccountId) {
        List<Installment> installments = getInstallments(loanAccountId);;

        Installment firstPendingInstallment = installments.stream()
                .filter(i -> i.getStatus() == InstallmentStatus.PENDING)
                .findFirst()
                .orElse(null);

        return installments.stream()
                .map(i -> installmentMapper.toInstallmentResponse
                        (i, firstPendingInstallment != null && firstPendingInstallment.getId().equals(i.getId())))
                .toList();
    }

    private List<Installment> getInstallments(UUID userId) {
        return installmentRepository.findByLoanAccountIdOrderByInstallmentNumberAsc(userId);
    }
}
