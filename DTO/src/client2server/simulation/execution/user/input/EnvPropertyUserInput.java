package client2server.simulation.execution.user.input;

public class EnvPropertyUserInput {
    private final int reqId;
    private final String name;
    private final boolean isRandomInit;
    private final Object value;

    public EnvPropertyUserInput(int reqId, String name, boolean isRandomInit, Object value) {
        this.reqId = reqId;
        this.name = name;
        this.isRandomInit = isRandomInit;
        this.value = value;
    }

    public int getReqId() {
        return reqId;
    }

    public String getName() {
        return name;
    }

    public boolean isRandomInit() {
        return isRandomInit;
    }

    public Object getValue() {
        return value;
    }
}
