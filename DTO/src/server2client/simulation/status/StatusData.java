package server2client.simulation.status;

import javafx.beans.property.SimpleStringProperty;

/**
 * Used for the execution queue component.
 */
public class StatusData {
    private final String simId;
    private final SimpleStringProperty status;

    public StatusData(String simId, SimpleStringProperty status) {
        this.simId = simId;
        this.status = status;
    }

    public StatusData(String simId, String status) {
        this.simId = simId;
        this.status = new SimpleStringProperty(status);
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
}
