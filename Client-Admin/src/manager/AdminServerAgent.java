package manager;

import gui.app.api.Controller;
import javafx.application.Platform;
import javafx.stage.Stage;
import manager.constant.Constants;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class AdminServerAgent {
    /**
     * Sends a http query that checks if an admin client is connected.
     * If an admin is not connected then allows this instance of the client to show and run.
     * Otherwise, shows an alert saying that an admin is already connected. Once the alert is closed
     * this client is also closed.
     *
     * @param controller We use this to access the notification bar and show notifications.
     */
    public static void connect(Controller controller, Stage primaryStage) {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_CONNECT_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendPostRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> controller.showAlertAndWait("Error: Could not reach server."));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                // If connection is successful, open the admin client application.
                if (response.code() == 200) {
                    controller.showAlertAndWait("Connection successful. Welcome Admin!");
                }

                // If an admin session is currently in progress, show an alert and close the app.
                else if (response.code() == 409) {
                    Platform.runLater(() -> {
                        controller.showAlertAndWait("An admin is already connected.");
                        System.exit(0);
                    });

                }
                // If another error has occurred, show an alert and close the app.
                else {
                    Platform.runLater(() -> {
                        controller.showAlertAndWait("Encountered a problem while trying to connect.");
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

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendDeleteRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showDisconnectErrorAndExit(controller);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    showDisconnectErrorAndExit(controller);
                }
            }
        });
    }

    private static void showDisconnectErrorAndExit(Controller controller) {
        controller.showAlertAndWait("An error has occurred while closing the program.");
        System.exit(0);
    }

    public static void uploadFile(File file, Controller controller) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        String finalUrl = HttpUrl
                .parse(Constants.FILE_UPLOAD_PATH)
                .newBuilder()
                .build()
                .toString();

        HttpClientAgent.sendPostRequest(finalUrl, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }
}
