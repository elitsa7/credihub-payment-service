package bg.credihub.payment.exceptions;

public class MissingMetaDataException extends RuntimeException {
    public MissingMetaDataException(String message) {
        super(message);
    }
}
