package gui.app;

import gui.api.UserEngineCommunicator;
import gui.api.BarNotifier;
import gui.api.HasFileLoadedListeners;
import gui.app.mode.AppMode;
import gui.app.notification.NotificationBarComponentController;
import gui.app.menu.MenuComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import manager.UserServerAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class UserAppController implements HasFileLoadedListeners, BarNotifier, UserEngineCommunicator {
    @FXML private BorderPane currentMainComponent;
    @FXML private GridPane headerComponent;
    @FXML private GridPane subMenus;
    @FXML private MenuComponentController subMenusController;

    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;

    @FXML private AnchorPane anchorNotification;

    @FXML private Label usernameLabel;
    public UserServerAgent engineAgent;

    private AppMode appMode;

    @FXML
    public void initialize() {
        engineAgent = new UserServerAgent();
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
    public UserServerAgent getEngineAgent() {
        return engineAgent;
    }

    public void setUsername(String username) {
        usernameLabel.setText(String.format("Hello %s",username));
    }

}
