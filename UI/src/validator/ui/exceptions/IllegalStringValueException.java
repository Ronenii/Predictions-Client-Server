package validator.ui.exceptions;

public class IllegalStringValueException extends Exception {
    private String message;

    public IllegalStringValueException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
