package server2client.simulation.queue;

import java.util.List;

public class AddedSimulationsData {

    String[] addedSimulations;

    public AddedSimulationsData(String[] addedSimulations) {
        this.addedSimulations = addedSimulations;
    }

    public AddedSimulationsData(List<String> addedSimulations){
        this.addedSimulations = addedSimulations.toArray(new String[0]);
    }

    public String[] getAddedSimulations() {
        return addedSimulations;
    }

    public void setAddedSimulations(String[] addedSimulations) {
        this.addedSimulations = addedSimulations;
    }
}
