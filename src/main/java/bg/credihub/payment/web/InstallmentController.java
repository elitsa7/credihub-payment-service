package bg.credihub.payment.web;

import bg.credihub.payment.models.dtos.CheckoutSessionResponse;
import bg.credihub.payment.models.dtos.InstallmentResponse;
import bg.credihub.payment.service.InstallmentService;
import bg.credihub.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/installments")
public class InstallmentController {
    private final InstallmentService installmentService;
    private final PaymentService paymentService;

    public InstallmentController(InstallmentService installmentService, PaymentService paymentService) {
        this.installmentService = installmentService;
        this.paymentService = paymentService;
    }

    @GetMapping("/loan/{loanAccountId}")
    public ResponseEntity<List<InstallmentResponse>> getLoanInstallments(@PathVariable UUID loanAccountId) {
        return ResponseEntity.ok(installmentService.getLoanInstallments(loanAccountId));
    }

    @PostMapping("/{installmentId}/checkout")
    public ResponseEntity<CheckoutSessionResponse> checkout(@PathVariable UUID installmentId) throws StripeException {
        CheckoutSessionResponse response = paymentService.createCheckoutSession(installmentId);
        return ResponseEntity.ok(response);
    }
}
