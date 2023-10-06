package server2client.simulation.load.success;

public class DTOLoadResult {
    private final boolean succeed;
    private final String message;

    public DTOLoadResult(boolean succeed, String message) {
        this.succeed = succeed;
        this.message = message;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public String getMessage() {
        return message;
    }
}
