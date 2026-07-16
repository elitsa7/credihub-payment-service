package bg.credihub.payment.gateway;

import bg.credihub.payment.models.dtos.CheckoutSessionResponse;
import bg.credihub.payment.models.enums.PaymentMethod;
import com.stripe.exception.StripeException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Profile({"dev", "test"})
public class MockPaymentGateway implements PaymentGateway{
    @Override
    public CheckoutSessionResponse createCheckoutSession(UUID paymentId, BigDecimal amount) throws StripeException {
        return CheckoutSessionResponse.builder()
                .sessionId("LOCAL")
                .checkoutUrl("/payments/success?session_id=LOCAL")
                .build();
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.LOCAL;
    }
}
