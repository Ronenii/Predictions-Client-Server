package server2client.simulation.runtime;

import server2client.simulation.genral.impl.objects.DTOEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the data of a simulation run after it ended.
 */
public class ResultData implements Serializable {

    private final Map<String, List<Integer>> populationChartData;

    private DTOEntity[] entities;

    /**
     * A ResultData Object will be generated once a simulation run is complete.
     * The time of the ResultData creation is the same as the simulation time's ending,
     * so we save this time as a string according to the required format.
     * We also generate a unique ID for the simulation run.
     */
    public ResultData() {
        populationChartData = new HashMap<>();
    }

    /**
     * To be used at the end of a simulation run. Sets the entities to the entities at the end of a simulation run.
     */
    public void setEntities(DTOEntity[] entities) {
        this.entities = entities;
    }

    public DTOEntity[] getEntities() {
        return entities;
    }

    /**
     * Each index in the list represents the population after a 20 tick interval
     * This function adds the population to the list creating another population record for this tick.
     */
    public void setPopulationRecord(String entityName, int population) {
        if (populationChartData.get(entityName) == null) {
            List<Integer> populationList = new ArrayList<>();
            populationChartData.put(entityName, populationList);
        }

        populationChartData.get(entityName).add(population);
    }

    public Map<String, List<Integer>> getPopulationChartData() {
        return populationChartData;
    }
}
