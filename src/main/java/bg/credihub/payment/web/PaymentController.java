package bg.credihub.payment.web;

import bg.credihub.payment.models.dto.CheckoutSessionResponse;
import bg.credihub.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{installmentId}/checkout")
    public ResponseEntity<CheckoutSessionResponse> checkout(@PathVariable UUID installmentId) throws StripeException {
        CheckoutSessionResponse response = paymentService.createCheckoutSession(installmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload,
                                              @RequestHeader("Stripe-Signature") String signature) throws Exception {

        paymentService.completePayment(payload, signature);
        return ResponseEntity.ok().build();
    }
}
