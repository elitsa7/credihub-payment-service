package bg.credihub.payment.gateway;

import bg.credihub.payment.models.dtos.CheckoutSessionResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
@Component
public class StripePaymentGateway implements PaymentGateway {
    @Value("${credihub.base-url}")
    private String credihubBaseUrl;

    @Override
    public CheckoutSessionResponse createCheckoutSession(UUID paymentId, BigDecimal amount) throws StripeException {

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(credihubBaseUrl + "/payments/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(credihubBaseUrl + "/payments/cancel")
                .putMetadata("paymentId", paymentId.toString())
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValueExact())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("CrediHub Loan Installment")
                                        .setDescription("Loan installment payment.")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

        return CheckoutSessionResponse.builder()
                .sessionId(session.getId())
                .checkoutUrl(session.getUrl())
                .build();
    }
}
