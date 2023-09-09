package engine2ui.simulation.execution;

public class SetResponse {

    private final boolean success;

    private final String message;

    public SetResponse(boolean success, String message){
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
