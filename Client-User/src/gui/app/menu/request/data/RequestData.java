package gui.app.menu.request.data;

import client2server.simulation.request.DTORequest;
import simulation.properties.property.impl.IntProperty;

public class RequestData {
    private final int requestId;
    private final String simulationName;
    private final int tokens;
    private RequestStatus status;
    private IntProperty running;
    private IntProperty finished;

    public RequestData(int requestId, DTORequest dtoRequest) {
        this.requestId = requestId;
        simulationName = dtoRequest.getSimulationName();
        tokens = dtoRequest.getSimulationTokens();
        status = RequestStatus.PENDING;
    }
}
