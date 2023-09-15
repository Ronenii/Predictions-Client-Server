package manager.execution;

import engine2ui.simulation.runtime.SimulationRunData;
import manager.DTO.creator.DTOCreator;
import simulation.objects.world.SimulationInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class possesses the program's thread pool and a map of simulations that have been added to the thread pool's queue.
 */
public class ExecutionManager {
    private ExecutorService threadExecutor = null;
    private final Map<String, SimulationInstance> simulations;
    private final Map<String, SimulationRunData> simulationsRunData;

    public ExecutionManager(int threadCount) {
        threadExecutor = Executors.newFixedThreadPool(threadCount);
        simulations = new HashMap<>();
        simulationsRunData = new HashMap<>();
    }

    public void addSimulationToQueue(SimulationInstance simulationInstance, SimulationRunData simulationRunData) {
        simulations.put(simulationInstance.getSimulationId(), simulationInstance);
        simulationsRunData.put(simulationRunData.getSimId(), simulationRunData);
        threadExecutor.execute(simulationInstance);
    }

    public SimulationInstance getSimulationById(String simId) {
        return simulations.get(simId);
    }

    public void shutdownThreadPool() {
        threadExecutor.shutdown();
    }

    /**
     * Return a new SimulationDataObject when the simulation is ongoing, otherwise return the SimulationRunData from the map.
     */
    public SimulationRunData getRunDataById(String simId) {
        SimulationInstance simulationInstance = simulations.get(simId);
        DTOCreator dtoCreator = new DTOCreator();
        SimulationRunData ret = null;

        switch (simulationInstance.getStatus()) {
            case ONGOING:
                ret = new SimulationRunData(simId, simulationInstance.getTicksCounter().getTicks(), simulationInstance.getTimePassed(), dtoCreator.getDTOEntityPopulationList(simulationInstance.getEntities()), "ONGOING", false);
                break;
            case WAITING:
                ret = simulationsRunData.get(simId);
                break;
            case COMPLETED:
                if(simulationsRunData.get(simId).status.equals("COMPLETED")) {
                    ret = simulationsRunData.get(simId);
                } else {
                    ret = new SimulationRunData(simId, simulationInstance.getTicksCounter().getTicks(), simulationInstance.getTimePassed(), dtoCreator.getDTOEntityPopulationList(simulationInstance.getEntities()), "COMPLETED", true);
                    ret.resultData = simulationInstance.getResultData();
                    simulationsRunData.put(simId, ret);
                }

                break;
        }

        return ret;
    }


}
