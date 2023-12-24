package server2client.simulation.runtime;

import server2client.simulation.genral.impl.objects.DTOEntityPopulation;

import java.util.Map;

public class SimulationRunData {
    private final String name;
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
    private final long threadSleepCount;

    public SimulationRunData(String name, String simId, int tick, long time, DTOEntityPopulation[] entityPopulation, String status, boolean isCompleted, Map<String, Object> envVarsValuesMap, boolean isSimulationSkipped, long threadSleepCount) {
        this.name = name;
        this.simId = simId;
        this.entityPopulation = entityPopulation;
        this.tick = tick;
        this.time = time;
        this.status = status;
        this.isCompleted = isCompleted;
        this.envVarsValuesMap = envVarsValuesMap;
        this.isSimulationSkipped = isSimulationSkipped;
        this.threadSleepCount = threadSleepCount;
    }

    public String getSimId() {
        return simId;
    }

    public String getName() {
        return name;
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

    public long getThreadSleepCount() {
        return threadSleepCount;
    }
}
