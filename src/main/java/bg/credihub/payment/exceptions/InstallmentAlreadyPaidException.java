package bg.credihub.payment.exceptions;

public class InstallmentAlreadyPaidException extends RuntimeException {
    public InstallmentAlreadyPaidException(String message) {
        super(message);
    }
}
