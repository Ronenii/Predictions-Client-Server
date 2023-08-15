package validator.ui.exceptions;

public class IllegalStringValueException extends Exception {
    private String message;

    public IllegalStringValueException(String value) {
        this.message =  "The string: " + value + "does not match the requested pattern.";;
    }

    public String getMessage() {
        return message;
    }
}
