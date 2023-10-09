package server2client.simulation.load.result;

/**
 * Contains info about an attempt to load a sim.
 * When succeeded, there is no need to save a message.
 * When it didn't succeed the message will contain the errors that occurred.
 */
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
