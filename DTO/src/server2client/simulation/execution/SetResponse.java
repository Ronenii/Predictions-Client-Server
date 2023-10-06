package server2client.simulation.execution;

public class SetResponse {

    private final boolean success;

    private final String message;

    private final Object parsedValue;

    public SetResponse(boolean success, String message, Object parsedValue) {
        this.success = success;
        this.message = message;
        this.parsedValue = parsedValue;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getParsedValue() {
        return parsedValue;
    }
}
