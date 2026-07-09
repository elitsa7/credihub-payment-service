package bg.credihub.payment.repository;

import bg.credihub.payment.models.entity.LoanAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, UUID> {
    List<LoanAccount> findByUserId(UUID userId);

    boolean existsByApplicationId(UUID applicationId);
}
