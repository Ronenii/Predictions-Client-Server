package engine2ui.simulation.runtime;

import engine2ui.simulation.genral.impl.objects.DTOEntity;

import java.util.List;

public class SimulationRunData {
    private final String simId;
    private final String currentTick;
    private final String currentElapsedTime;
    private final List<DTOEntity> entities;

    private final String status;

    public SimulationRunData(String simId, String currentTick, String currentElapsedTime, List<DTOEntity> entities, String status) {
        this.simId = simId;
        this.currentTick = currentTick;
        this.currentElapsedTime = currentElapsedTime;
        this.entities = entities;
        this.status = status;
    }

    public String getSimId() {
        return simId;
    }

    public String getCurrentTick() {
        return currentTick;
    }

    public String getCurrentElapsedTime() {
        return currentElapsedTime;
    }

    public List<DTOEntity> getEntities() {
        return entities;
    }

    public String getStatus() {
        return status;
    }
}
