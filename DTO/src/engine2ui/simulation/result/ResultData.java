package engine2ui.simulation.result;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Holds the data of a simulation run after it ended.
 */
public class ResultData {

    String dateTime;
    UUID id;

    /**
     * A ResultData Object will be generated once a simulation run is complete.
     * The time of the ResultData creation is the same as the simulation time's ending,
     * so we save this time as a string according to the required format.
     * We also generate a unique ID for the simulation run.
     */
    public ResultData(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        LocalDateTime runTime = LocalDateTime.now();
        dateTime = dtf.format(runTime);
        id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return String.format("ID: %s\n",id) + String.format("Date & Time: %s\n", dateTime);
    }
}
