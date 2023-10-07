package manager;

import engine2ui.simulation.execution.SetResponse;
import engine2ui.simulation.execution.StartResponse;
import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.app.login.LoginComponentController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import manager.constants.Constants;
import manager.exception.SimulationNotLoadedException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import ui2engine.simulation.control.bar.DTOSimulationControlBar;
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;
import ui2engine.simulation.execution.user.input.EnvPropertyUserInput;
import ui2engine.simulation.load.DTOLoadFile;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;
import java.util.List;

public class UserServerAgent {

    private final EngineInterface engine;

    private boolean isFileLoaded;

    public UserServerAgent() {
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






    public static void connect(String username, LoginComponentController loginComponentController) {
        String finalUrl = HttpUrl
                .parse(Constants.CLIENT_CONNECT_PATH)
                .newBuilder()
                .addQueryParameter("username", username)
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendPostRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> loginComponentController.setErrorMessage("Could not reach server. Trying again."));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                // If connection is successful, open the admin client application.
                if (response.code() == 200) {
                    Platform.runLater(loginComponentController::setLoggedIn);
                }

                // If an admin session is currently in progress, show an alert and close the app.
                else if (response.code() == 409) {
                    Platform.runLater(() -> loginComponentController.setErrorMessage("This username is already taken."));
                }
                // If another error has occurred, show an alert and close the app.
                else {
                    Platform.runLater(() -> loginComponentController.setErrorMessage("Please insert a username (At least one character)"));
                }
            }
        });
    }
}
