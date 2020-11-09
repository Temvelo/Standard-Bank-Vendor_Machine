package za.co.standardbank.vendingmachine.exception;

public class NotFullPaidException extends RuntimeException {
    public NotFullPaidException(String message) {
        super(message);
    }
}
