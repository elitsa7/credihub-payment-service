package bg.credihub.payment.repository;

import bg.credihub.payment.models.entities.Payment;
import bg.credihub.payment.models.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    boolean existsByInstallment_IdAndStatus(UUID installmentId, PaymentStatus status);
}
