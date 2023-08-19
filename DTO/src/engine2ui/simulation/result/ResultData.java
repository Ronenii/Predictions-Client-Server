package engine2ui.simulation.result;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.NonRangedDTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.RangedDTOProperty;
import engine2ui.simulation.result.generator.IdGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Holds the data of a simulation run after it ended.
 */
public class ResultData {

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

    // TODO: This is for debugging
    public ResultData(String dateTime, DTOEntity [] entities)
    {
        id = IdGenerator.generateID();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        this.dateTime = LocalDateTime.parse(dateTime, dtf);
        this.entities = entities;
    }

//    // TODO: This is for debugging
//    public ResultData(String dateTime)
//    {
//        entities = new DTOEntity[3];
//
//        // SMOKER
//        List<DTOProperty> smokerProperties = new ArrayList<>();
//        smokerProperties.add(new RangedDTOProperty("lung-cancer-progress", "decimal",false, 0, 100));
//        smokerProperties.add(new RangedDTOProperty("age", "decimal",false, 15, 50));
//        smokerProperties.add(new RangedDTOProperty("cigarets-per-month", "decimal",true, 0, 500));
//        entities[0] = new DTOEntity("Smoker",100, 15,smokerProperties.toArray(new DTOProperty[0]));
//
//        // NON SMOKER
//        List<DTOProperty> nonSmokerProperties = new ArrayList<>();
//        nonSmokerProperties.add(new RangedDTOProperty("lung-cancer-progress", "decimal",false, 0, 100));
//        nonSmokerProperties.add(new RangedDTOProperty("age","decimal", false, 15, 50 ));
//        nonSmokerProperties.add(new NonRangedDTOProperty("is-working-out", "boolean",true));
//        entities[1] = new DTOEntity("Non Smoker", 100, 41, nonSmokerProperties.toArray(new DTOProperty[0]));
//
//        // CHILD
//        List<DTOProperty> childProperties = new ArrayList<>();
//        childProperties.add(new RangedDTOProperty("lung-cancer-progress", "decimal", false, 0, 100));
//        childProperties.add(new RangedDTOProperty("age", "decimal", false, 0, 14));
//        childProperties.add(new NonRangedDTOProperty("eating-sugar","boolean", true));
//        entities[2] = new DTOEntity("Child", 50, 45, childProperties.toArray(new DTOProperty[0]));
//
//
//        id = IdGenerator.generateID();
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
//        this.dateTime = LocalDateTime.parse(dateTime, dtf);
//    }

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
