package gui.app;

import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.api.HasFileLoadedListeners;
import gui.header.component.HeaderComponentController;
import gui.notification.NotificationBarComponentController;
import gui.result.models.QueueManagementData;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import manager.EngineAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AppController implements HasFileLoadedListeners, BarNotifier, EngineCommunicator {
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
        engineAgent = new EngineAgent();
        if(headerComponentController != null && subMenusController != null && notificationBarComponentController != null) {
            headerComponentController.setMainController(this);
            subMenusController.setMainController(this);
            notificationBarComponentController.setMainController(this);
        }
    }

    public void setPrimaryStageOnClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            engineAgent.shutdownThreadPool();
        });
    }

    public List<EventListener> getAllFileLoadedListeners(){
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(headerComponentController);
        listeners.addAll(subMenusController.getAllFileLoadedListeners());
        return listeners;
    }

    /**
     * Shows the given string on the notification bar.
     */
    @Override
    public void showNotification(String notification){
        anchorNotification.visibleProperty().set(true);
        notificationBarComponentController.addNotification(notification);
    }

    @Override
    public BarNotifier getNotificationBar() {
        return this;
    }

    @Override
    public EngineAgent getEngineAgent() {
        return engineAgent;
    }

    public void updateQueueLblInQueueManagement() {
        headerComponentController.updateQueueLblInQueueManagement();
    }

    public void updateRunningAndCompletedLblsInQueueManagement(QueueManagementData queueManagementData) {
        headerComponentController.updateRunningAndCompletedLblsInQueueManagement(queueManagementData);
    }

}
