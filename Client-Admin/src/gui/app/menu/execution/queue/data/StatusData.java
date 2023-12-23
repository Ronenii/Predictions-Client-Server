package gui.app.menu.execution.queue.data;

import javafx.beans.property.SimpleStringProperty;

/**
 * Used for the execution queue component.
 */
public class StatusData {
    private final String requestedBy;
    private final String simId;
    private final SimpleStringProperty status;

    public StatusData(String requestedBy, String simId, SimpleStringProperty status) {
        this.simId = simId;
        this.status = status;
        this.requestedBy = requestedBy;
    }

    public StatusData(String requestedBy, String simId, String status) {
        this.simId = simId;
        this.status = new SimpleStringProperty(status);
        this.requestedBy = requestedBy;
    }

    public String getSimId() {
        return simId;
    }

    public String getStatus() {
        return status.get();
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
