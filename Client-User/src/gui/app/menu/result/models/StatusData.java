package gui.app.menu.result.models;

import gui.app.menu.request.data.RequestData;
import javafx.beans.property.SimpleStringProperty;

/**
 * Used for the execution queue component.
 */
public class StatusData {
    private final String simId;
    private final SimpleStringProperty status;
    private final RequestData requestData;
    private boolean isRunningUpdated;
    private boolean isFinishedUpdated;

    public StatusData(String simId, SimpleStringProperty status, RequestData requestData) {
        this.simId = simId;
        this.status = status;
        this.requestData = requestData;
        isRunningUpdated = false;
        isFinishedUpdated = false;
    }

    public String getSimId() {
        return simId;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRunningUpdated(boolean runningUpdated) {
        isRunningUpdated = runningUpdated;
    }

    public boolean isRunningUpdated() {
        return isRunningUpdated;
    }

    public boolean isFinishedUpdated() {
        return isFinishedUpdated;
    }

    public void setFinishedUpdated(boolean finishedUpdated) {
        isFinishedUpdated = finishedUpdated;
    }
}
