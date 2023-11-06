package client2server.simulation.request;

import server2client.simulation.genral.impl.properties.DTOEndingCondition;

public class DTORequest {
    private final String simulationName;
    private final int simulationTokens;
    private final DTOEndingCondition[] endingConditions;

    public DTORequest(String simulationName, int simulationTokens, DTOEndingCondition[] endingConditions) {
        this.simulationName = simulationName;
        this.simulationTokens = simulationTokens;
        this.endingConditions = endingConditions;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public int getSimulationTokens() {
        return simulationTokens;
    }

    public DTOEndingCondition[] getEndingConditions() {
        return endingConditions;
    }
}
