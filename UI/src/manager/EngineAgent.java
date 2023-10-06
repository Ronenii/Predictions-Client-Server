package manager;

import server2client.simulation.execution.SetResponse;
import server2client.simulation.execution.StartResponse;
import server2client.simulation.load.success.DTOLoadResult;
import server2client.simulation.runtime.SimulationRunData;
import javafx.concurrent.Task;
import manager.exception.SimulationNotLoadedException;
import client2server.simulation.control.bar.DTOSimulationControlBar;
import client2server.simulation.execution.user.input.EntityPopulationUserInput;
import client2server.simulation.execution.user.input.EnvPropertyUserInput;
import client2server.simulation.load.DTOLoadFile;

import java.io.File;

import java.util.*;

/**
 * Responsible for UI communication with the Engine module. Sends data to the Engine, receives results from Engine accordingly,
 * Prints the data out using the Console class.
 */
public class EngineAgent {
    private final EngineInterface engine;

    private boolean isFileLoaded;

    public EngineAgent() {
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

//    /**
//     * prompts the user to input a path to a simulation XML config file and loads it
//     * into the system.
//     */
//    public void loadSimulationFromFile(File file, List<EventListener> listeners) {
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                DTOLoadResult dtoLoadSucceed = engine.loadSimulationFromFile(new DTOLoadFile(file, listeners));
//
//                 /* If we succeeded in creating the simulation we want to reset the engine.
//                    If a simulation was loaded beforehand and the creation failed we don't want
//                    to delete past data until a new simulation is loaded successfully. */
//                if (dtoLoadSucceed.isSucceed()) {
//                    isFileLoaded = true;
//                    engine.resetEngine();
//                }
//                return null;
//            }
//        };
//
//        runTask(task);
//    }

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
