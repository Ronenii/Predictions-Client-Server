package fixed.app;

import fixed.api.UserEngineCommunicator;
import gui.api.BarNotifier;
import gui.api.HasFileLoadedListeners;
import gui.app.mode.AppMode;
import fixed.app.notification.NotificationBarComponentController;
import fixed.app.menu.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import manager.UserEngineAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AppController implements HasFileLoadedListeners, BarNotifier, UserEngineCommunicator {
    @FXML private BorderPane currentMainComponent;
    @FXML private GridPane headerComponent;
    @FXML private GridPane subMenus;
    @FXML private SubMenusController subMenusController;

    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;

    @FXML private AnchorPane anchorNotification;
    public UserEngineAgent engineAgent;

    private AppMode appMode;

    @FXML
    public void initialize() {
        engineAgent = new UserEngineAgent();
        if(subMenusController != null && notificationBarComponentController != null) {
            subMenusController.setMainController(this);
            notificationBarComponentController.setMainController(this);
        }

        anchorNotification.visibleProperty().set(true);
        appMode = AppMode.DEFAULT;
    }

//    public void setPrimaryStageOnClose(Stage primaryStage) {
//        primaryStage.setOnCloseRequest(event -> {
//            engineAgent.shutdownThreadPool();
//        });
//    }


    public List<EventListener> getAllFileLoadedListeners(){
        List<EventListener> listeners = new ArrayList<>();
        listeners.addAll(subMenusController.getAllFileLoadedListeners());
        return listeners;
    }

    /**
     * Shows the given string on the notification bar.
     */
    @Override
    public void showNotification(String notification){
        notificationBarComponentController.turnOnNotificationBar();
        notificationBarComponentController.addNotification(notification);
    }

    @Override
    public BarNotifier getNotificationBar() {
        return this;
    }

    @Override
    public UserEngineAgent getEngineAgent() {
        return engineAgent;
    }

}
