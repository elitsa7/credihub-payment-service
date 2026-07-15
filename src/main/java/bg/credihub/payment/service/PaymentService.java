package bg.credihub.payment.service;

import bg.credihub.payment.exceptions.*;
import bg.credihub.payment.gateway.PaymentGateway;
import bg.credihub.payment.models.dtos.CheckoutSessionResponse;
import bg.credihub.payment.models.entities.Installment;
import bg.credihub.payment.models.entities.LoanAccount;
import bg.credihub.payment.models.entities.Payment;
import bg.credihub.payment.models.enums.InstallmentStatus;
import bg.credihub.payment.models.enums.LoanStatus;
import bg.credihub.payment.models.enums.PaymentMethod;
import bg.credihub.payment.models.enums.PaymentStatus;
import bg.credihub.payment.repository.InstallmentRepository;
import bg.credihub.payment.repository.LoanAccountRepository;
import bg.credihub.payment.repository.PaymentRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final InstallmentRepository installmentRepository;
    private final LoanAccountRepository loanAccountRepository;
    private final PaymentGateway paymentGateway;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public PaymentService(PaymentRepository paymentRepository, InstallmentRepository installmentRepository, LoanAccountRepository loanAccountRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.installmentRepository = installmentRepository;
        this.loanAccountRepository = loanAccountRepository;
        this.paymentGateway = paymentGateway;
    }

    public CheckoutSessionResponse createCheckoutSession(UUID installmentId) throws StripeException {
        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new InstallmentNotFoundException("Installment not found."));

        validateInstallment(installment);
        validatePayable(installment);

        Payment payment = createPendingPayment(installment);

        CheckoutSessionResponse response =
                paymentGateway.createCheckoutSession(
                        payment.getId(),
                        payment.getAmount());

        payment.setStripeSessionId(response.getSessionId());

        paymentRepository.save(payment);

        return response;
    }

    public void completePayment(String payload, String signature) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, signature, webhookSecret);

        if (!"checkout.session.completed".equals(event.getType())) {
            return;
        }

        Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();

        String paymentIdValue = session.getMetadata().get("paymentId");

        if (paymentIdValue == null) {
            throw new MissingMetaDataException("Missing paymentId metadata.");
        }

        UUID paymentId = UUID.fromString(paymentIdValue);

        Payment payment = paymentRepository.findById(paymentId).orElseThrow();

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setTransactionReference(session.getPaymentIntent());
        paymentRepository.save(payment);

        Installment installment = payment.getInstallment();
        installment.setStatus(InstallmentStatus.PAID);
        installment.setPaidAt(LocalDateTime.now());

        installmentRepository.save(installment);

        LoanAccount loanAccount = installment.getLoanAccount();

        loanAccount.setPaidInstallments(loanAccount.getPaidInstallments() + 1);

        loanAccount.setRemainingBalance(loanAccount.getRemainingBalance().subtract(payment.getAmount()));

        if (loanAccount.getPaidInstallments().equals(loanAccount.getPeriodMonths())) {
            loanAccount.setStatus(LoanStatus.CLOSED);
        }

        loanAccountRepository.save(loanAccount);
    }

    private void validateInstallment(Installment installment) {
        if (installment.getStatus() == InstallmentStatus.PAID) {
            throw new InstallmentAlreadyPaidException("Installment is already paid.");
        }

        if (paymentRepository.existsByInstallment_IdAndStatus(installment.getId(), PaymentStatus.PENDING)) {
            throw new PendingPaymentAlreadyExistsException("There is already a pending payment for this installment.");
        }
    }

    private void validatePayable(Installment installment) {
        Installment firstPending = installmentRepository.findFirstByLoanAccountIdAndStatusOrderByInstallmentNumberAsc(
                installment.getLoanAccount().getId(),
                InstallmentStatus.PENDING).orElseThrow(() -> new InvalidPaymentException("No pending installments."));

        if (!firstPending.getId().equals(installment.getId())) {
            throw new InvalidPaymentException("This installment cannot be paid yet.");
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
