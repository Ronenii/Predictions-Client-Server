package manager.execution;

import manager.SimulationManager;
import simulation.objects.world.SimulationInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionManager {
    private ExecutorService threadExecutor = null;
    private final Map<String, SimulationInstance> simulations;

    public ExecutionManager(int threadCount) {
        threadExecutor = Executors.newFixedThreadPool(threadCount);
        simulations = new HashMap<>();
    }

    public void addSimulationToQueue(SimulationInstance simulationInstance) {
        simulations.put(simulationInstance.getSimulationId(), simulationInstance);
        threadExecutor.execute(simulationInstance);
    }

    public SimulationInstance getSimulationById(String simId) {
        return simulations.get(simId);
    }

    public void shutdownThreadPool() {
        threadExecutor.shutdown();
    }
}
