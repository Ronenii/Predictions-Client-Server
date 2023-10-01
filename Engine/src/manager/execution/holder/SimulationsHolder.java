package manager.execution.holder;

import engine2ui.simulation.runtime.SimulationRunData;
import simulation.objects.world.SimulationInstance;

import java.util.HashMap;
import java.util.Map;

public class SimulationsHolder {
    private final Map<String, SimulationInstance> simulationInstances;

    private final Map<String, SimulationRunData> simulationsRunData;

    public SimulationsHolder() {
        this.simulationInstances = new HashMap<>();
        this.simulationsRunData = new HashMap<>();
    }

    public Map<String, SimulationInstance> getSimulationInstances() {
        return simulationInstances;
    }

    public void addSimulationInstance(SimulationInstance simulationInstance) {
        simulationInstances.put(simulationInstance.getSimulationId(), simulationInstance);
    }

    public void addSimulationRunData(SimulationRunData simulationRunData) {
        simulationsRunData.put(simulationRunData.getSimId(), simulationRunData);
    }

}
