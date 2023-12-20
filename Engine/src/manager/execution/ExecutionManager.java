package manager.execution;

import server2client.simulation.runtime.SimulationRunData;
import manager.DTO.creator.DTOCreator;
import simulation.objects.world.SimulationInstance;
import simulation.objects.world.user.instructions.UserInstructions;
import client2server.simulation.control.bar.DTOSimulationControlBar;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class possesses the program's thread pool and a map of simulations that have been added to the thread pool's queue.
 */
public class ExecutionManager {
    private ExecutorService threadExecutor;
    private final Map<String, SimulationInstance> simulations;
    private final Map<String, SimulationRunData> simulationsRunData;
    private boolean isSkippingForward;

    public ExecutionManager(int threadCount) {
        threadExecutor = Executors.newFixedThreadPool(threadCount);
        simulations = new HashMap<>();
        simulationsRunData = new HashMap<>();
        isSkippingForward = false;
    }

    public void addSimulationToQueue(SimulationInstance simulationInstance, SimulationRunData simulationRunData) {
        if(simulationInstance != null) {
            simulations.put(simulationInstance.getSimulationId(), simulationInstance);
            simulationsRunData.put(simulationRunData.getSimId(), simulationRunData);
            threadExecutor.execute(simulationInstance);
        }
    }


    public void shutdownThreadPool() {
        threadExecutor.shutdown();
    }

    public void shutdownPreviousThreadPoolAndSetNewThreadPool(int threadCount) {
        threadExecutor.shutdown();
        threadExecutor = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Return a new SimulationDataObject when the simulation is ongoing, otherwise return the SimulationRunData from the map.
     */
    public synchronized SimulationRunData getRunDataById(String simId) {
        SimulationInstance simulationInstance = simulations.get(simId);
        DTOCreator dtoCreator = new DTOCreator();
        SimulationRunData ret = null;

        switch (simulationInstance.getStatus()) {
            case ONGOING:
                if(isSkippingForward){
                    ret = new SimulationRunData(simId, simulationInstance.getTicksCounter().getTicks(), simulationInstance.getTimePassed(), dtoCreator.getDTOEntityPopulationArray(simulationInstance.getEntities()), "ONGOING", false, null, true, simulationInstance.getThreadSleepDuration());
                    ret.resultData = simulationInstance.getResultData();
                } else {
                    ret = new SimulationRunData(simId, simulationInstance.getTicksCounter().getTicks(), simulationInstance.getTimePassed(), dtoCreator.getDTOEntityPopulationArray(simulationInstance.getEntities()), "ONGOING", false, null, false, simulationInstance.getThreadSleepDuration());
                }
                break;
            case WAITING:
                ret = simulationsRunData.get(simId);
                break;
            case COMPLETED:
                if(simulationsRunData.get(simId).status.equals("COMPLETED")) {
                    ret = simulationsRunData.get(simId);
                } else {
                    ret = new SimulationRunData(simId, simulationInstance.getTicksCounter().getTicks(), simulationInstance.getTimePassed(), dtoCreator.getDTOEntityPopulationArray(simulationInstance.getEntities()), "COMPLETED", true, null,false, simulationInstance.getThreadSleepDuration());
                    ret.resultData = simulationInstance.getResultData();
                    simulationsRunData.put(simId, ret);
                }

                break;
            case CRUSHED:
                if(simulationsRunData.get(simId).status.equals("CRUSHED")) {
                    simulationsRunData.get(simId).errorMessage = null;
                    ret = simulationsRunData.get(simId);
                } else {
                    ret = new SimulationRunData(simId, simulationInstance.getTicksCounter().getTicks(), simulationInstance.getTimePassed(), dtoCreator.getDTOEntityPopulationArray(simulationInstance.getEntities()), "CRUSHED", false, null, false, simulationInstance.getThreadSleepDuration());
                    ret.errorMessage = simulationInstance.getErrorMessage();
                    simulationsRunData.put(simId, ret);
                }

                break;
        }

        return ret;
    }

    /**
     * set the simulations control bar flags according to the 'DTOSimulationControlBar'.
     * The 'DTOSimulationControlBar' contains info about which button was clicked.
     */
    public void setStopPausePlayOrSkipFwdForSimById(String simId, DTOSimulationControlBar dtoSimulationControlBar) {
        SimulationInstance simulationInstance = simulations.get(simId);
        UserInstructions userInstructions = simulationInstance.getUserInstructions();

        if(dtoSimulationControlBar.isToPause()) {
            userInstructions.isSimulationPaused = true;
            userInstructions.isSimulationRunning = false;
        } else if (dtoSimulationControlBar.isToPlay()) {
            userInstructions.isSimulationRunning = true;
            isSkippingForward = false;
            userInstructions.isSimulationPaused = false;
        } else if (dtoSimulationControlBar.isToStop()) {
            userInstructions.isSimulationStopped = true;
            userInstructions.isSimulationRunning = false;
            isSkippingForward = false;
        }

        if (dtoSimulationControlBar.isToSkipForward()) {
            userInstructions.isSimulationSkippedForward = true;
            isSkippingForward = true;
        }
    }
}
