package engine2ui.simulation.result;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.NonRangedDTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.RangedDTOProperty;
import engine2ui.simulation.result.generator.IdGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Holds the data of a simulation run after it ended.
 */
public class ResultData implements Serializable {

    private final LocalDateTime dateTime;
    private final String id;
    private final DTOEntity[] entities;

    /**
     * A ResultData Object will be generated once a simulation run is complete.
     * The time of the ResultData creation is the same as the simulation time's ending,
     * so we save this time as a string according to the required format.
     * We also generate a unique ID for the simulation run.
     */
    public ResultData(DTOEntity [] entities){
        dateTime = LocalDateTime.now();
        id = IdGenerator.generateID();
        this.entities = entities;
    }

    /**
     * Clears all IDs generated in the ID generator of the result data.
     */
    public static void clearIds(){
        IdGenerator.clearIds();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDateTimeString()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        return dtf.format(dateTime);
    }

    public DTOEntity[] getEntities() {
        return entities;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("ID: %s\n",id) + String.format("Date & Time: %s\n", getDateTimeString());
    }

}
