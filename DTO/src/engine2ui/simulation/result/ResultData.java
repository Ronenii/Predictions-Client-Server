package engine2ui.simulation.result;

import engine2ui.simulation.result.generator.IdGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;

/**
 * Holds the data of a simulation run after it ended.
 */
public class ResultData {

    private final LocalDateTime dateTime;
    private final String id;

    /**
     * A ResultData Object will be generated once a simulation run is complete.
     * The time of the ResultData creation is the same as the simulation time's ending,
     * so we save this time as a string according to the required format.
     * We also generate a unique ID for the simulation run.
     */
    public ResultData(){
        dateTime = LocalDateTime.now();
        id = IdGenerator.generateID();
    }

    // TODO: This is for debugging
    public ResultData(String dateTime)
    {
        id = IdGenerator.generateID();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        this.dateTime = LocalDateTime.parse(dateTime, dtf);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDateTimeString()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        return dtf.format(dateTime);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("ID: %s\n",id) + String.format("Date & Time: %s\n", getDateTimeString());
    }

}
