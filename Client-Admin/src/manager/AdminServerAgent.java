package manager;

import gui.app.AdminAppController;
import javafx.application.Platform;
import manager.constant.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AdminServerAgent {
    private static boolean isConnected = false;

    /**
     * Tries to connect the admin app to the server.
     *
     * @param adminAppController We use this to access the notification bar and show notifications.
     */
    public static boolean connect(AdminAppController adminAppController) {
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
                // Connection successful
                if (response.code() == 200) {
                    String responseBody = response.body().string();
                    adminAppController.showNotification("Connection successful. Welcome Admin!");
                    isConnected = true;
                }
                // An admin session is currently in progress
                else if (response.code() == 409) {
                    adminAppController.showPushNotification("An admin is already connected.");
                    isConnected = false;
                }
                // Another error
                else {
                    adminAppController.showPushNotification("Encountered a problem while trying to connect.");
                    isConnected = false;
                }
            }
        });
        return isConnected;
    }
}
