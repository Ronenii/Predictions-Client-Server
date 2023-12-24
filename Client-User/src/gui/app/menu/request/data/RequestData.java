package gui.app.menu.request.data;

import client2server.simulation.request.DTORequest;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class created to hold the request data for the controllers that use this data, such as the request table component and the execution queue component.
 */
public class RequestData {
    private final int requestId;
    private final String simulationName;
    private SimpleIntegerProperty tokens;
    private SimpleStringProperty status;
    private SimpleIntegerProperty running;
    private SimpleIntegerProperty finished;

    public RequestData(int requestId, DTORequest dtoRequest) {
        this.requestId = requestId;
        simulationName = dtoRequest.getSimulationName();
        tokens = new SimpleIntegerProperty(dtoRequest.getSimulationTokens());
        status = new SimpleStringProperty("PENDING");
        running = new SimpleIntegerProperty(0);
        finished = new SimpleIntegerProperty(0);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public int getRequestId() {
        return requestId;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public int getTokens() {
        return tokens.get();
    }

    public void decreaseTokens(){
        tokens.set(tokens.getValue() - 1);
    }

    public String getStatus() {
        return status.get();
    }

    public void increaseRunning(){
        running.set(running.getValue() + 1);
    }

    public void decreaseRunning() {
        running.set(running.getValue() - 1);
    }

    public void increaseFinished() {
        finished.set(finished.getValue() + 1);
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
