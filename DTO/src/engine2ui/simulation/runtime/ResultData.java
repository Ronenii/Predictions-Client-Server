package engine2ui.simulation.runtime;

import engine2ui.simulation.genral.impl.objects.DTOEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Holds the data of a simulation run after it ended.
 */
public class ResultData implements Serializable {

    private Map<Integer, List<DTOEntity>> graphData;

    private final DTOEntity[] entities;

    /**
     * A ResultData Object will be generated once a simulation run is complete.
     * The time of the ResultData creation is the same as the simulation time's ending,
     * so we save this time as a string according to the required format.
     * We also generate a unique ID for the simulation run.
     */
    public ResultData(DTOEntity [] entities){
        this.entities = entities;
    }

    public DTOEntity[] getEntities() {
        return entities;
    }
}
