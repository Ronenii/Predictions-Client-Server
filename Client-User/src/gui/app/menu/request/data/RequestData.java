package gui.app.menu.request.data;

import simulation.properties.property.impl.IntProperty;

public class RequestData {
    private int requestId;
    private final String simulationName;
    private final int tokens;
    private RequestStatus status;
    private IntProperty running;
    private IntProperty finished;

    public RequestData(String simulationName, int tokens, RequestStatus status) {
        this.simulationName = simulationName;
        this.tokens = tokens;
        this.status = status;
    }
}
