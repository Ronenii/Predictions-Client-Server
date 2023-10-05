package manager;

import gui.app.AdminAppController;
import javafx.application.Platform;
import javafx.stage.Stage;
import manager.constant.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AdminServerAgent {
    /**
     * Sends a http query that checks if an admin client is connected.
     * If an admin is not connected then allows this instance of the client to show and run.
     * Otherwise, shows an alert saying that an admin is already connected. Once the alert is closed
     * this client is also closed.
     *
     * @param adminAppController We use this to access the notification bar and show notifications.
     */
    public static void connect(AdminAppController adminAppController, Stage primaryStage) {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_CONNECT_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> adminAppController.showNotification("Error: Could not reach server. Trying again."));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // If connection is successful, open the admin client application.
                if (response.code() == 200) {
                    String responseBody = response.body().string();
                    adminAppController.showNotification("Connection successful. Welcome Admin!");
                    Platform.runLater(primaryStage::show);
                }

                // If an admin session is currently in progress, show an alert and close the app.
                else if (response.code() == 409) {
                    Platform.runLater(() -> {
                        adminAppController.showAlert("An admin is already connected.");
                        System.exit(0);
                    });

                }
                // If another error has occurred, show an alert and close the app.
                else {
                    Platform.runLater(() -> {
                        adminAppController.showAlert("Encountered a problem while trying to connect.");
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
     * @param adminAppController We use this to show alerts.
     */
    public static void disconnect(AdminAppController adminAppController) {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_DISCONNECT_PATH)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New Request for: " + finalUrl);

        HttpClientAgent.sendRequest(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showDisconnectErrorAndExit(adminAppController);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    showDisconnectErrorAndExit(adminAppController);
                }
            }
        });
    }

    private static void showDisconnectErrorAndExit(AdminAppController adminAppController){
        adminAppController.showNotification("An error has occurred while closing the program.");
        System.exit(0);
    }
}
