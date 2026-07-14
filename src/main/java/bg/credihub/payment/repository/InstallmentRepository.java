package bg.credihub.payment.repository;

import bg.credihub.payment.models.entities.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, UUID> {
    List<Installment> findByLoanAccountIdOrderByInstallmentNumberAsc(UUID loanAccountId);
}
