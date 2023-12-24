package gui.app;

import gui.app.api.Controller;
import gui.app.menu.MenuComponentController;
import gui.app.notification.NotificationBarComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import manager.AdminServerAgent;
import server2client.simulation.admin.load.details.AdminLoadDetails;

public class AdminAppController implements Controller {
    @FXML private TabPane menuComponent;
    @FXML private MenuComponentController menuComponentController;
    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;
    @FXML private AnchorPane anchorNotification;
    public AdminServerAgent engineAgent;

    public void setMainController(Controller controller) {
    }

    @FXML
    public void initialize() {
        engineAgent = new AdminServerAgent();
        if(notificationBarComponentController != null && menuComponentController != null) {
            notificationBarComponentController.setMainController(this);
            menuComponentController.setMainController(this);
        }

        anchorNotification.visibleProperty().set(true);
        AdminServerAgent.getAdminLoadDetails(this);
    }

    public void receiveAdminLoadDetails(AdminLoadDetails adminLoadDetails) {
        menuComponentController.receiveAdminLoadDetails(adminLoadDetails);
    }

    public void setPrimaryStageOnClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
        });
    }


    @Override
    public void showMessageInNotificationBar(String message) {
        notificationBarComponentController.turnOnNotificationBar();
        notificationBarComponentController.showMessageInNotificationBar(message);
    }

}
