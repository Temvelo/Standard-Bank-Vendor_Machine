package za.co.standardbank.vendingmachine.exception;

public class SoldOutException extends RuntimeException {
    public SoldOutException(String message) {
        super(message);
    }
}
