package bg.credihub.payment.gateway;

import bg.credihub.payment.models.dto.CheckoutSessionResponse;
import com.stripe.exception.StripeException;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentGateway {

    CheckoutSessionResponse createCheckoutSession(UUID paymentId, BigDecimal amount) throws StripeException;
}
