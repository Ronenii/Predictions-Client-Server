package gui.app;

import gui.api.Controller;
import gui.app.notification.NotificationBarComponentController;
import gui.app.menu.MenuComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class UserAppController implements Controller {
    @FXML private BorderPane currentMainComponent;
    @FXML private GridPane headerComponent;
    @FXML private GridPane subMenus;
    @FXML private MenuComponentController subMenusController;

    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;

    @FXML private AnchorPane anchorNotification;

    @FXML private Label usernameLabel;

    @FXML
    public void initialize() {
        if(subMenusController != null && notificationBarComponentController != null) {
            subMenusController.setMainController(this);
            notificationBarComponentController.setMainController(this);
        }

        anchorNotification.visibleProperty().set(true);
    }

    public void setUsername(String username) {
        usernameLabel.setText(String.format("Hello %s",username));
        showMessageInNotificationBar(String.format("%s has connected successfully", username));
    }

    /**
     * Shows the given string on the notification bar.
     */
    @Override
    public void showMessageInNotificationBar(String message) {
        notificationBarComponentController.turnOnNotificationBar();
        notificationBarComponentController.addNotification(message);
    }
}
