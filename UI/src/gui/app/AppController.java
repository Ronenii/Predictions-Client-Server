package gui.app;

import gui.notification.NotificationBarComponentController;
import gui.header.component.HeaderComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;

public class AppController {
    @FXML private GridPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;
    @FXML private GridPane subMenus;
    @FXML private SubMenusController subMenusController;

    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;

    @FXML private AnchorPane anchorNotification;

    public EngineAgent engineAgent;

    @FXML
    public void initialize() {
        if(headerComponentController != null && subMenusController != null && notificationBarComponentController != null) {
            headerComponentController.setMainController(this);
            subMenusController.setMainController(this);
            notificationBarComponentController.setMainController(this);
            engineAgent = new EngineAgent();
        }
    }

    public void showNotification(String notification){
        anchorNotification.visibleProperty().set(true);
        notificationBarComponentController.setLblNotificationText(notification);
    }
}
