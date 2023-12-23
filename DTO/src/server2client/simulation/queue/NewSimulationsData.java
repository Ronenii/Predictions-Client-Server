package server2client.simulation.queue;

import java.util.List;

public class NewSimulationsData {

    private final SimulationData[] addedSimulations;

    public NewSimulationsData(SimulationData[] addedSimulations) {
        this.addedSimulations = addedSimulations;
    }


    public NewSimulationsData(List<SimulationData> addedSimulations){
        this.addedSimulations = addedSimulations.toArray(new SimulationData[0]);
    }

    public SimulationData[] getAddedSimulations() {
        return addedSimulations;
    }
}
