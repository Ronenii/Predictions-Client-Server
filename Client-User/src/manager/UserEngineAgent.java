package manager;

import engine2ui.simulation.execution.SetResponse;
import engine2ui.simulation.execution.StartResponse;
import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.runtime.SimulationRunData;
import javafx.concurrent.Task;
import manager.exception.SimulationNotLoadedException;
import ui2engine.simulation.control.bar.DTOSimulationControlBar;
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;
import ui2engine.simulation.execution.user.input.EnvPropertyUserInput;
import ui2engine.simulation.load.DTOLoadFile;

import java.io.File;
import java.util.EventListener;
import java.util.List;

public class UserEngineAgent {

    private final EngineInterface engine;

    private boolean isFileLoaded;

    public UserEngineAgent() {
        isFileLoaded = false;
        this.engine = new SimulationManager();
    }

    public boolean isFileLoaded() {
        return isFileLoaded;
    }

    /**
     * Gets the current simulation details from the engine and prints it.
     */
    public void showCurrentSimulationDetails() throws SimulationNotLoadedException {
        if (!engine.getIsSimulationLoaded()) {
            throw new SimulationNotLoadedException("There is no simulation loaded in the system.");
        }
        //Console.showSimulationDetails(engine.getCurrentSimulationDetails());
    }

    /**
     * prompts the user to input a path to a simulation XML config file and loads it
     * into the system.
     */
    public void loadSimulationFromFile(File file, List<EventListener> listeners) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DTOLoadSucceed dtoLoadSucceed = engine.loadSimulationFromFile(new DTOLoadFile(file, listeners));

                 /* If we succeeded in creating the simulation we want to reset the engine.
                    If a simulation was loaded beforehand and the creation failed we don't want
                    to delete past data until a new simulation is loaded successfully. */
                if (dtoLoadSucceed.isSucceed()) {
                    isFileLoaded = true;
                    engine.resetEngine();
                }
                return null;
            }
        };

        runTask(task);
    }

    private void runTask(Task<Void> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true); // Mark the thread as a daemon to allow application exit
        thread.start();
    }


    public SetResponse sendPopulationData(EntityPopulationUserInput input) {
        return engine.setEntityPopulation(input);
    }

    public SetResponse sendEnvironmentVariableData(EnvPropertyUserInput input) {
        return engine.setEnvironmentVariable(input);
    }

    public StartResponse startSimulation() {
        return engine.startSimulation();
    }

    public synchronized SimulationRunData getRunDataById(String simId) {
        return engine.getRunDataById(simId);
    }

    public void shutdownThreadPool() {
        engine.shutdownThreadPool();
    }

    public void setStopPausePlayOrSkipFwdForSimById(String simId, DTOSimulationControlBar dtoSimulationControlBar) {
        engine.setStopPausePlayOrSkipFwdForSimById(simId, dtoSimulationControlBar);
    }
}
