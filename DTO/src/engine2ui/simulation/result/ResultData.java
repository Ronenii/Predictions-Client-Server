package engine2ui.simulation.result;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.runtime.generator.IdGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds the data of a simulation run after it ended.
 */
public class ResultData implements Serializable {

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

    /**
     * Clears all IDs generated in the ID generator of the result data.
     */
    public static void clearIds(){
        IdGenerator.clearIds();
    }

    public DTOEntity[] getEntities() {
        return entities;
    }
}
