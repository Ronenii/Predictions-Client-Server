package engine2ui.simulation.runtime;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class SimulationRunData {
    private final String simId;
    public final SimpleStringProperty status;
    private final SimpleStringProperty currentTick;
    private final SimpleStringProperty currentElapsedTime;
    private final List<DTOEntity> entities;

    public SimulationRunData(String simId, String currentTick, String currentElapsedTime, List<DTOEntity> entities, String status) {
        this.simId = simId;
        this.entities = entities;
        this.currentTick = new SimpleStringProperty(currentTick);
        this.currentElapsedTime = new SimpleStringProperty(currentElapsedTime);
        this.status = new SimpleStringProperty(status);
    }

    public String getSimId() {
        return simId;
    }

    public SimpleStringProperty getCurrentTick() {
        return currentTick;
    }

    public SimpleStringProperty getCurrentElapsedTime() {
        return currentElapsedTime;
    }

    public List<DTOEntity> getEntities() {
        return entities;
    }

    public SimpleStringProperty getStatus() {
        return status;
    }
}
