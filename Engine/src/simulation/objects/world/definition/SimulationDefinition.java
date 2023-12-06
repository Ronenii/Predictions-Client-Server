package simulation.objects.world.definition;

import simulation.objects.world.SimulationInstance;

public class SimulationDefinition {
    private final String simulationName;
    private final SimulationInstance simulationAbstractInstance;

    public SimulationDefinition(String simulationName, SimulationInstance simulationAbstractInstance) {
        this.simulationName = simulationName;
        this.simulationAbstractInstance = simulationAbstractInstance;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public SimulationInstance getSimulationAbstractInstance() {
        return simulationAbstractInstance;
    }

}
