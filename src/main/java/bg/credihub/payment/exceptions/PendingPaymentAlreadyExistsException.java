package bg.credihub.payment.exceptions;

public class PendingPaymentAlreadyExistsException extends RuntimeException {
    public PendingPaymentAlreadyExistsException(String message) {
        super(message);
    }
}
