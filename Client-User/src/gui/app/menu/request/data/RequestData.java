package gui.app.menu.request.data;

import client2server.simulation.request.DTORequest;
import javafx.beans.property.SimpleIntegerProperty;
import simulation.properties.property.impl.IntProperty;

public class RequestData {
    private final int requestId;
    private final String simulationName;
    private final int tokens;
    private String status;
    private SimpleIntegerProperty running;
    private SimpleIntegerProperty finished;

    public RequestData(int requestId, DTORequest dtoRequest) {
        this.requestId = requestId;
        simulationName = dtoRequest.getSimulationName();
        tokens = dtoRequest.getSimulationTokens();
        status = "PENDING";
        running = new SimpleIntegerProperty();
        finished = new SimpleIntegerProperty();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRunning(int running) {
        this.running.set(running);
    }

    public void setFinished(int finished) {
        this.finished.set(finished);
    }

    public int getRequestId() {
        return requestId;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public int getTokens() {
        return tokens;
    }

    public String getStatus() {
        return status;
    }

    public int getRunning() {
        return running.get();
    }

    public SimpleIntegerProperty runningProperty() {
        return running;
    }

    public int getFinished() {
        return finished.get();
    }

    public SimpleIntegerProperty finishedProperty() {
        return finished;
    }
}
