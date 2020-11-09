package za.co.standardbank.vendingmachine.exception;

public class NotSufficientChangeException extends RuntimeException {
    public NotSufficientChangeException(String message) {
        super(message);
    }
}
