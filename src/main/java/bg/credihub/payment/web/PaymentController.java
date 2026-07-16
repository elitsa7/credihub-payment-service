package bg.credihub.payment.web;

import bg.credihub.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload,
                                              @RequestHeader("Stripe-Signature") String signature) throws Exception {

        paymentService.completePayment(payload, signature);
        return ResponseEntity.ok().build();
    }
}
