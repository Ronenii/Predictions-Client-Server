package server2client.simulation.runtime;

import server2client.simulation.genral.impl.objects.DTOEntityPopulation;

import java.util.List;
import java.util.Map;

public class SimulationRunData {
    // Todo - convert this object to be more simple - without using lists/maps.
    private final String simId;
    public final String status;
    private final int tick;
    private final long time;
    private final DTOEntityPopulation[] entityPopulation;
    public ResultData resultData;
    private final boolean isCompleted;
    private final Map<String, Object> envVarsValuesMap;
    public String errorMessage = null;
    private final boolean isSimulationSkipped;

    public SimulationRunData(String simId, int tick, long time, DTOEntityPopulation[] entityPopulation, String status, boolean isCompleted, Map<String, Object> envVarsValuesMap, boolean isSimulationSkipped) {
        this.simId = simId;
        this.entityPopulation = entityPopulation;
        this.tick = tick;
        this.time = time;
        this.status = status;
        this.isCompleted = isCompleted;
        this.envVarsValuesMap = envVarsValuesMap;
        this.isSimulationSkipped = isSimulationSkipped;
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

    public DTOEntityPopulation[] getEntityPopulation() {
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

    public Map<String, Object> getEnvVarsValuesMap() {
        return envVarsValuesMap;
    }

    public boolean isSimulationSkipped() {
        return isSimulationSkipped;
    }
}
