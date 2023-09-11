package engine2ui.simulation.execution;

public class StartResponse {
    private final boolean success;

    private final String message;

    public StartResponse(boolean success, String message){
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
