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

    public void setEntities(DTOEntity[] entities){
        this.entities = entities;
    }

    public DTOEntity[] getEntities() {
        return entities;
    }

    public void setNextTickPopulation(int population){
        populationChartData.add(population);
    }

    public List<Integer> getPopulationChartData(){
        return populationChartData;
    }
}
