package manager;

import client2server.simulation.request.DTORequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gui.api.Controller;
import gui.app.menu.execution.control.bar.ControlBarController;
import gui.app.menu.execution.inputs.InputsController;
import gui.app.menu.execution.inputs.entity.EntityPopulationComponentController;
import gui.app.menu.execution.inputs.env.var.EnvironmentVariableComponentController;
import gui.app.menu.request.create.request.NewRequestComponentController;
import gui.app.menu.request.data.RequestData;
import gui.app.menu.request.table.RequestTableComponentController;
import gui.app.menu.result.models.StatusData;
import gui.app.menu.result.queue.ExecutionQueueComponentController;
import gui.app.menu.simulation.breakdown.SimBreakdownMenuController;
import manager.constants.Constants;
import okhttp3.*;
import server2client.simulation.execution.SetResponse;
import server2client.simulation.execution.StartResponse;
import server2client.simulation.genral.impl.objects.DTOEntity;
import server2client.simulation.genral.impl.properties.DTOEnvironmentVariable;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.prview.SimulationsPreviewData;
import server2client.simulation.request.updated.status.DTORequestStatusUpdate;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.login.LoginComponentController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import client2server.simulation.control.bar.DTOSimulationControlBar;
import client2server.simulation.execution.user.input.EntityPopulationUserInput;
import client2server.simulation.execution.user.input.EnvPropertyUserInput;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;


public class UserServerAgent {

    private final SimulationManager engine;

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
//    public void showCurrentSimulationDetails() throws SimulationNotLoadedException {
//        if (!engine.getIsSimulationLoaded()) {
//            throw new SimulationNotLoadedException("There is no simulation loaded in the system.");
//        }
//        //Console.showSimulationDetails(engine.getCurrentSimulationDetails());
//    }

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

    public static void updateSimBreakdown(SimBreakdownMenuController simBreakdownMenuController) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalUrl = HttpUrl
                .parse(Constants.SIMULATIONS_DETAILS_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendGetRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> simBreakdownMenuController.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If connection is successful, open the admin client application.
                if (response.code() == 200) {
                    String previewDataInJson = response.body().string();
                    // if the servlet returns empty string, the simulation breakdown is up-to-date.
                    if(!previewDataInJson.equals("")){
                        SimulationsPreviewData simulationsPreviewData = gson.fromJson(previewDataInJson, SimulationsPreviewData.class);
                        Platform.runLater(() -> simBreakdownMenuController.updateSimTreeView(simulationsPreviewData));
                    }
                }
                // If another error has occurred, show an alert and close the app.
                else {
                    Platform.runLater(() -> simBreakdownMenuController.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    /**
     * The method return the request id from the server
     */
    public static void sendSimulationRequest(NewRequestComponentController controller, DTORequest dtoRequest) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String requestJsonContent = gson.toJson(dtoRequest);
        RequestBody requestBody = RequestBody.create(requestJsonContent,MediaType.parse("application/json"));
        String finalUrl = HttpUrl
                .parse(Constants.SIMULATION_REQUEST_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendPostRequest(finalUrl, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    String meow = response.body().string();
                    int requestId = Integer.parseInt(meow);
                    Platform.runLater(() -> {
                        controller.addNewRequestData(requestId, dtoRequest);
                        controller.showMessageInNotificationBar("New request has been sent!");
                    });
                } else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    public static void getRequestsStatusUpdates(RequestTableComponentController controller) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalUrl = HttpUrl
                .parse(Constants.REQUEST_STATUS_UPDATE_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendGetRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    String requestStatusUpdateInJson = response.body().string();
                    DTORequestStatusUpdate dtoRequestStatusUpdate = gson.fromJson(requestStatusUpdateInJson, DTORequestStatusUpdate.class);
                    Platform.runLater(() -> controller.updateRequestsStatus(dtoRequestStatusUpdate));
                } else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    public static void getSimulationPreviewDataForExecutionWindow(InputsController controller, RequestData requestData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalUrl = HttpUrl
                .parse(Constants.SINGLE_SIMULATION_PREVIEW_DATA_PATH)
                .newBuilder()
                .addQueryParameter("simulationName", requestData.getSimulationName())
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendGetRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    String requestStatusUpdateInJson = response.body().string();
                    PreviewData previewData = gson.fromJson(requestStatusUpdateInJson, PreviewData.class);
                    Platform.runLater(() -> controller.setUpExecutionWindowWithPreviewData(previewData, requestData));
                } else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    public static void sendPopulationData(EntityPopulationComponentController controller, EntityPopulationUserInput input, DTOEntity selectedItem, boolean isInit) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String requestJsonContent = gson.toJson(input);
        RequestBody requestBody = RequestBody.create(requestJsonContent,MediaType.parse("application/json"));
        String finalUrl = HttpUrl
                .parse(Constants.SEND_POPULATION_DATA_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendPostRequest(finalUrl, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    if(!isInit) {
                        String requestStatusUpdateInJson = response.body().string();
                        SetResponse setResponse = gson.fromJson(requestStatusUpdateInJson, SetResponse.class);
                        Platform.runLater(() -> controller.receiveSetResponse(setResponse, selectedItem, input.getPopulation()));
                    }
                } else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    public static void sendEnvironmentVariableData(EnvironmentVariableComponentController controller, EnvPropertyUserInput input, DTOEnvironmentVariable selectedItem, String value, boolean isInit) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String requestJsonContent = gson.toJson(input);
        RequestBody requestBody = RequestBody.create(requestJsonContent,MediaType.parse("application/json"));
        String finalUrl = HttpUrl
                .parse(Constants.SEND_ENV_VARS_DATA_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendPostRequest(finalUrl, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200) {
                    if(!isInit) {
                        String requestStatusUpdateInJson = response.body().string();
                        SetResponse setResponse = gson.fromJson(requestStatusUpdateInJson, SetResponse.class);
                        Platform.runLater(() -> controller.receiveSetResponse(setResponse, selectedItem, value));
                    }
                } else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    public static void startSimulation(ControlBarController controller, int reqId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalUrl = HttpUrl
                .parse(Constants.START_SIMULATION_PATH)
                .newBuilder()
                .addQueryParameter("requestId", String.valueOf(reqId))
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendPostRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String requestStartResponseInJson = response.body().string();
                    StartResponse startResponse = gson.fromJson(requestStartResponseInJson, StartResponse.class);
                    Platform.runLater(() -> controller.receiveStartResponse(startResponse));
                } else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    public static void getSimRunDataForSimStatus(ExecutionQueueComponentController controller, StatusData statusData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalUrl = HttpUrl
                .parse(Constants.GET_SIMULATION_RUN_DATA_PATH)
                .newBuilder()
                .addQueryParameter("simId", String.valueOf(statusData.getSimId()))
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendGetRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String requestStartResponseInJson = response.body().string();
                    SimulationRunData runData = gson.fromJson(requestStartResponseInJson, SimulationRunData.class);
                    Platform.runLater(() -> controller.statusUpdateForSingleRunningSimulation(runData, statusData));

                } else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }
}
