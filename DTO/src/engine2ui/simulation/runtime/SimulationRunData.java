package engine2ui.simulation.runtime;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.objects.DTOEntityPopulation;

import java.util.List;

public class SimulationRunData {
    private final String simId;
    public final String status;
    private final int tick;
    private final long time;
    private final List<DTOEntityPopulation> entityPopulation;
    public ResultData resultData;
    private final boolean isCompleted;

    public SimulationRunData(String simId, int tick, long time, List<DTOEntityPopulation> entityPopulation, String status, boolean isCompleted) {
        this.simId = simId;
        this.entityPopulation = entityPopulation;
        this.tick = tick;
        this.time = time;
        this.status = status;
        this.isCompleted = isCompleted;
    }

    public String getSimId() {
        return simId;
    }

    public int getTick() {
        return tick;
    }

    public long getTime() {
        return time;
    }

    public List<DTOEntityPopulation> getEntityPopulation() {
        return entityPopulation;
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