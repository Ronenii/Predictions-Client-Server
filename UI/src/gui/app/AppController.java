package gui.app;

import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.api.HasFileLoadedListeners;
import gui.app.mode.AppMode;
import gui.header.component.HeaderComponentController;
import gui.notification.NotificationBarComponentController;
import gui.result.models.QueueManagementData;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import manager.EngineAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AppController implements HasFileLoadedListeners, BarNotifier, EngineCommunicator {
    @FXML private BorderPane currentMainComponent;
    @FXML private GridPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;
    @FXML private GridPane subMenus;
    @FXML private SubMenusController subMenusController;

    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;

    @FXML private AnchorPane anchorNotification;
    public EngineAgent engineAgent;

    private AppMode appMode;

    @FXML
    public void initialize() {
        engineAgent = new EngineAgent();
        if(headerComponentController != null && subMenusController != null && notificationBarComponentController != null) {
            headerComponentController.setMainController(this);
            subMenusController.setMainController(this);
            notificationBarComponentController.setMainController(this);
        }

        anchorNotification.visibleProperty().set(true);
        appMode = AppMode.DEFAULT;
    }

    public void setPrimaryStageOnClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            engineAgent.shutdownThreadPool();
        });
    }

    public void getSceneForThemes(Scene scene){
        headerComponentController.setSceneForThemes(scene);
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
        notificationBarComponentController.turnOnNotificationBar();
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

    public void changeToDarkMode() {
        clearMode();
        headerComponentController.changeToDarkMode();
        subMenusController.changeToDarkMode();
        notificationBarComponentController.changeToDarkMode();
        currentMainComponent.getStylesheets().add(getClass().getResource("themes/DarkMode.css").toExternalForm());
        appMode = AppMode.DARK;
    }

    public void changeToLightMode() {
        clearMode();
        headerComponentController.changeToLightMode();
        subMenusController.changeToLightMode();
        notificationBarComponentController.changeToLightMode();
        currentMainComponent.getStylesheets().add(getClass().getResource("themes/LightMode.css").toExternalForm());
        appMode = AppMode.LIGHT;
    }

    public void clearMode() {
        headerComponentController.clearMode(appMode);
        subMenusController.clearMode(appMode);
        notificationBarComponentController.clearMode(appMode);

        switch (appMode) {
            case DARK:
                currentMainComponent.getStylesheets().remove(getClass().getResource("themes/DarkMode.css").toExternalForm());
                break;
            case LIGHT:
                currentMainComponent.getStylesheets().remove(getClass().getResource("themes/LightMode.css").toExternalForm());
                break;
        }

        appMode = AppMode.DEFAULT;
    }

}
