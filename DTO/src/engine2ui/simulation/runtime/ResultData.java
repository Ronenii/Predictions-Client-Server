package engine2ui.simulation.runtime;

import engine2ui.simulation.genral.impl.objects.DTOEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the data of a simulation run after it ended.
 */
public class ResultData implements Serializable {

    private List<Integer> populationChartData;

    private DTOEntity[] entities;

    /**
     * A ResultData Object will be generated once a simulation run is complete.
     * The time of the ResultData creation is the same as the simulation time's ending,
     * so we save this time as a string according to the required format.
     * We also generate a unique ID for the simulation run.
     */
    public ResultData(){
        populationChartData = new ArrayList<>();
    }

    /**
     * To be used at the end of a simulation run. Sets the entities to the entities at the end of a simulation run.
     */
    public void setEntities(DTOEntity[] entities){
        this.entities = entities;
    }

    public DTOEntity[] getEntities() {
        return entities;
    }

    /**
     * Each index in the list represents the tick, the content within each index is the total living population at that tick.
     * This function adds the population to the list creating another tick documentation.
     */
    public void setNextTickPopulation(int population){
        populationChartData.add(population);
    }

    public List<Integer> getPopulationChartData(){
        return populationChartData;
    }
}
