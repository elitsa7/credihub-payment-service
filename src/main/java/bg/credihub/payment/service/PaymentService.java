package bg.credihub.payment.service;

import bg.credihub.payment.exceptions.InstallmentAlreadyPaidException;
import bg.credihub.payment.exceptions.InstallmentNotFoundException;
import bg.credihub.payment.exceptions.PendingPaymentAlreadyExistsException;
import bg.credihub.payment.gateway.PaymentGateway;
import bg.credihub.payment.models.dto.CheckoutSessionResponse;
import bg.credihub.payment.models.entity.Installment;
import bg.credihub.payment.models.entity.Payment;
import bg.credihub.payment.models.enums.InstallmentStatus;
import bg.credihub.payment.models.enums.PaymentMethod;
import bg.credihub.payment.models.enums.PaymentStatus;
import bg.credihub.payment.repository.InstallmentRepository;
import bg.credihub.payment.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final InstallmentRepository installmentRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository, InstallmentRepository installmentRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.installmentRepository = installmentRepository;
        this.paymentGateway = paymentGateway;
    }

    public CheckoutSessionResponse createCheckoutSession(UUID installmentId) throws StripeException {
        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new InstallmentNotFoundException("Installment not found."));

        validateInstallment(installment);

        Payment payment = createPendingPayment(installment);

        CheckoutSessionResponse response =
                paymentGateway.createCheckoutSession(
                        payment.getId(),
                        payment.getAmount());

        payment.setStripeSessionId(response.getSessionId());

        paymentRepository.save(payment);

        return response;


    }

    private void validateInstallment(Installment installment) {
        if (installment.getStatus() == InstallmentStatus.PAID) {
            throw new InstallmentAlreadyPaidException("Installment is already paid.");
        }

        if (paymentRepository.existsByInstallment_IdAndStatus(installment.getId(), PaymentStatus.PENDING)) {
            throw new PendingPaymentAlreadyExistsException("There is already a pending payment for this installment.");
        }
    }

    private Payment createPendingPayment(Installment installment) {

        Payment payment = Payment.builder()
                .installment(installment)
                .amount(installment.getAmount())
                .paymentMethod(PaymentMethod.STRIPE)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }
}
