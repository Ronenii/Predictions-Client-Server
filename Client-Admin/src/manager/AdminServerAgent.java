package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.deploy.net.HttpResponse;
import gui.app.api.Controller;
import gui.app.menu.allocation.AllocationComponentController;
import gui.app.menu.management.simulation.SimulationManagerComponentController;
import gui.app.menu.management.thread.ThreadManagerComponentController;
import javafx.application.Platform;
import manager.constant.Constants;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import server2client.simulation.prview.SimulationsPreviewData;
import server2client.simulation.request.DTORequests;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class AdminServerAgent {
    /**
     * Sends a http query that checks if an admin client is connected.
     * If an admin is not connected then allows this instance of the client to show and run.
     * Otherwise, shows an alert saying that an admin is already connected. Once the alert is closed
     * this client is also closed.
     *
     * @param controller We use this to access the notification bar and show notifications.
     */
    public static void connect(Controller controller) {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_CONNECT_PATH)
                .newBuilder()
                .build()
                .toString();

        HttpClientAgent.sendPostRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showAlertAndWait("Error: Could not reach server."));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                // If connection is successful, open the admin client application.
                if (response.code() == 200) {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("Connection successful. Welcome Admin!"));
                    System.out.println("Admin connection successful.");
                }

                // If an admin session is currently in progress, show an alert and close the app.
                else if (response.code() == 409) {
                    Platform.runLater(() -> {
                        Platform.runLater(() -> controller.showAlertAndWait("An admin is already connected."));
                        System.out.println("An admin is already connected.");
                        System.exit(0);
                    });

                }
                // If another error has occurred, show an alert and close the app.
                else {
                    Platform.runLater(() -> {
                        Platform.runLater(() -> controller.showAlertAndWait("Encountered a problem while trying to connect."));
                        System.out.println("Encountered a problem while trying to connect.");
                        System.exit(0);
                    });
                }
            }
        });
    }

    /**
     * This function is called only if this instance of the admin client is the "connected" instance.
     * When this instance is closed by the user, we launch a http query signaling the server that the admin
     * has disconnected, allowing another instance of the admin client to connect.
     *
     * @param controller We use this to show alerts.
     */
    public static void disconnect(Controller controller) {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_DISCONNECT_PATH)
                .newBuilder()
                .build()
                .toString();

        HttpClientAgent.sendDeleteRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showDisconnectErrorAndExit(controller);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    showDisconnectErrorAndExit(controller);
                    System.out.println("Encountered a problem while admin tried to disconnect.");
                } else {
                    System.out.println("Admin successfully disconnected.");
                }
            }
        });
    }

    private static void showDisconnectErrorAndExit(Controller controller) {
        Platform.runLater(() -> {
            controller.showAlertAndWait("An error has occurred while closing the program.");
            System.exit(0);
        });

    }

    /**
     * Sends the given file within a post request (multipart body).
     * OK - Show file was loaded successfully and pull all loaded simulations to the listview in the SimulationManagerComponent.
     * BAD_REQUEST - Show the errors created from loading a bad simulation config file.
     * OTHER - Show a general error.
     *
     * TODO: I have given it some thought and we actually do need to send the file name to the engine since that is how
     *       we recognize that the client is trying to load a file that already exists.
     */
    public static void uploadFile(File file, SimulationManagerComponentController simulationManagerComponentController) {
        final String fileNameString = "File: \"" + file.getName() + "\", ";

        // TODO: Validate file path and suffix (xml)

        // Convert the file into a multipart request body
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),  RequestBody.create(MediaType.parse("application/xml"), file))
                .build();

        String finalUrl = HttpUrl
                .parse(Constants.FILE_UPLOAD_PATH)
                .newBuilder()
                .build()
                .toString();

        HttpClientAgent.sendPostRequest(finalUrl, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> simulationManagerComponentController.showMessageInNotificationBar("Error: could not reach server while trying to upload file."));
                System.out.println(fileNameString + "encountered a problem while uploading a file.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If load succeeded.
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    System.out.println(fileNameString + "uploaded successfully.");
                    Platform.runLater(() -> {
                        simulationManagerComponentController.showMessageInNotificationBar(fileNameString + "successfully uploaded to server.");
                        updateSimBreakdownList(simulationManagerComponentController);
                    });
                }
                // If the configuration file is invalid (errors with trying to load the file as a simulation).
                else if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    System.out.println(fileNameString + "Invalid file configuration.");
                    Platform.runLater(() -> simulationManagerComponentController.showMessageInNotificationBar(response.body().toString()));
                } else {
                    System.out.println(fileNameString + "encountered a problem while uploading a file.");
                    Platform.runLater(() -> simulationManagerComponentController.showMessageInNotificationBar("Error: a problem was encountered while trying to upload a file to the server."));
                }
            }
        });
    }

    public static void updateSimBreakdownList(SimulationManagerComponentController simulationManagerComponentController) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalUrl = HttpUrl
                .parse(Constants.SIMULATIONS_DETAILS_CONTEXT_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendGetRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> simulationManagerComponentController.showMessageInNotificationBar("An error occurred"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If connection is successful, open the admin client application.
                if (response.code() == 200) {
                    String previewDataInJson = response.body().string();
                    // if the servlet returns empty string, the simulation breakdown is up-to-date.
                    if(!previewDataInJson.equals("")){
                        SimulationsPreviewData simulationsPreviewData = gson.fromJson(previewDataInJson, SimulationsPreviewData.class);
                        Platform.runLater(() -> simulationManagerComponentController.loadSimulationsListView(simulationsPreviewData));
                    }
                }
                // If another error has occurred, show an alert and close the app.
                else {
                    Platform.runLater(() -> simulationManagerComponentController.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    public static void sendSimulationThreadCount(Controller controller, String simName, int threadCount) {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_THREAD_COUNT)
                .newBuilder()
                .addQueryParameter("simName", simName)
                .addQueryParameter("threadCount", String.valueOf(threadCount))
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
                // If connection is successful, open the admin client application.
                if (response.code() == 200) {
                    Platform.runLater(() -> controller.showMessageInNotificationBar(String.format("'%s' thread count updated", simName)));
                }
                // If another error has occurred, show an alert and close the app.
                else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

    public static void updateRequestsTable(AllocationComponentController controller) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_REQUEST_REFRESHER)
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
                if (response.code() == 200) {
                    String dtoRequestsInJson = response.body().string();
                    // check if the string json is empty.
                    if(!dtoRequestsInJson.equals("")) {
                        DTORequests dtoRequests = gson.fromJson(dtoRequestsInJson, DTORequests.class);
                        Platform.runLater(() -> controller.updateAllocationTableView(dtoRequests));
                    }
                } else {
                    Platform.runLater(() -> controller.showMessageInNotificationBar("An error occurred"));
                }
            }
        });
    }

}
