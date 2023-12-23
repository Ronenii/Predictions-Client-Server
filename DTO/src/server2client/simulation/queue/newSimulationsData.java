package server2client.simulation.queue;

import java.util.List;

public class newSimulationsData {

    String[] addedSimulations;

    public newSimulationsData(String[] addedSimulations) {
        this.addedSimulations = addedSimulations;
    }

    public newSimulationsData(List<String> addedSimulations){
        this.addedSimulations = addedSimulations.toArray(new String[0]);
    }

    public String[] getAddedSimulations() {
        return addedSimulations;
    }

    public void setAddedSimulations(String[] addedSimulations) {
        this.addedSimulations = addedSimulations;
    }
}
