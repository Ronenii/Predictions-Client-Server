package engine2ui.simulation.runtime;

import engine2ui.simulation.genral.impl.objects.DTOEntity;

import java.util.List;

public class SimulationRunData {
    private final String simId;
    public final String status;
    private final int tick;
    private final float time;
    private final List<DTOEntity> entities;
    public ResultData resultData;
    private final boolean isCompleted;

    public SimulationRunData(String simId, int tick, float time, List<DTOEntity> entities, String status, boolean isCompleted) {
        this.simId = simId;
        this.entities = entities;
        this.tick = 0;
        this.time = 0f;
        this.status = status;
        this.isCompleted = isCompleted;
    }

    public String getSimId() {
        return simId;
    }

    public int getTick() {
        return tick;
    }

    public float getTime() {
        return time;
    }

    public List<DTOEntity> getEntities() {
        return entities;
    }

    public String getStatus() {
        return status;
    }

    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }

    public ResultData getResultData() {
        return resultData;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

}
