package server2client.simulation.prview;

import server2client.simulation.genral.impl.objects.DTOEntity;
import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.genral.impl.properties.DTOGridAndThread;
import server2client.simulation.genral.impl.properties.DTORule;
import server2client.simulation.genral.impl.properties.DTOEnvironmentVariable;

import java.util.List;

/**
 * A class that holds the simulation's data required to present in menu option 2.
 */
public class PreviewData{
    private final String simulationName;
    private final DTOGridAndThread gridAndThread;
    private final DTOEnvironmentVariable[] envVariables;
    private final DTOEntity[] entities;
    private final DTORule[] rules;
    private final DTOEndingCondition[] endingConditions;

    public PreviewData(String simulationName, DTOGridAndThread gridAndThread, DTOEnvironmentVariable[] envVariables, DTOEntity[] entities, DTORule[] rules, DTOEndingCondition[] endingConditions) {
        this.simulationName = simulationName;
        this.gridAndThread = gridAndThread;
        this.envVariables = envVariables;
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public DTOGridAndThread getGridAndThread() {
        return gridAndThread;
    }

    public DTOEnvironmentVariable[] getEnvVariables() {
        return envVariables;
    }

    public DTOEntity[] getEntities() {
        return entities;
    }

    public DTORule[] getRules() {
        return rules;
    }

    public DTOEndingCondition[] getEndingConditions() {
        return endingConditions;
    }
}
