package bg.credihub.payment.repository;

import bg.credihub.payment.models.entities.Installment;
import bg.credihub.payment.models.enums.InstallmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, UUID> {
    List<Installment> findByLoanAccountIdOrderByInstallmentNumberAsc(UUID loanAccountId);

    Optional<Installment> findFirstByLoanAccountIdAndStatusOrderByInstallmentNumberAsc(UUID id, InstallmentStatus installmentStatus);
}
