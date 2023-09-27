package gui.app.menu.managment.simulation.data;

public class LoadedSimulationData {
    private final String simulationName;
    private final String simulationId;

    public LoadedSimulationData(String simulationName, String simulationId) {
        this.simulationName = simulationName;
        this.simulationId = simulationId;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getSimulationId() {
        return simulationId;
    }
}
