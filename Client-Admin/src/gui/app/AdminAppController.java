package gui.app;

import gui.app.notification.NotificationBarComponentController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import manager.AdminEngineAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AdminAppController{
    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;
    @FXML private AnchorPane anchorNotification;
    public AdminEngineAgent engineAgent;

    @FXML
    public void initialize() {
        engineAgent = new AdminEngineAgent();
        if(notificationBarComponentController != null) {
            notificationBarComponentController.setMainController(this);
        }

        anchorNotification.visibleProperty().set(true);
    }

    public void setPrimaryStageOnClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            engineAgent.shutdownThreadPool();
        });
    }


    public List<EventListener> getAllFileLoadedListeners(){
        // TODO: PLACEHOLDER
        return null;
    }

//    /**
//     * Shows the given string on the notification bar.
//     */
//    @Override
//    public void showNotification(String notification){
//        notificationBarComponentController.turnOnNotificationBar();
//        notificationBarComponentController.addNotification(notification);
//    }

//    public void updateQueueLblInQueueManagement() {
//        headerComponentController.updateQueueLblInQueueManagement();
//    }
//
//    public void updateRunningAndCompletedLblsInQueueManagement(QueueManagementData queueManagementData) {
//        headerComponentController.updateRunningAndCompletedLblsInQueueManagement(queueManagementData);
//    }
//
//    public void changeToDarkMode() {
//        clearMode();
//        headerComponentController.changeToDarkMode();
//        subMenusController.changeToDarkMode();
//        notificationBarComponentController.changeToDarkMode();
//        currentMainComponent.getStylesheets().add(getClass().getResource("themes/DarkMode.css").toExternalForm());
//        appMode = AppMode.DARK;
//    }
//
//    public void changeToLightMode() {
//        clearMode();
//        headerComponentController.changeToLightMode();
//        subMenusController.changeToLightMode();
//        notificationBarComponentController.changeToLightMode();
//        currentMainComponent.getStylesheets().add(getClass().getResource("themes/LightMode.css").toExternalForm());
//        appMode = AppMode.LIGHT;
//    }
//
//    public void clearMode() {
//        headerComponentController.clearMode(appMode);
//        subMenusController.clearMode(appMode);
//        notificationBarComponentController.clearMode(appMode);
//
//        switch (appMode) {
//            case DARK:
//                currentMainComponent.getStylesheets().remove(getClass().getResource("themes/DarkMode.css").toExternalForm());
//                break;
//            case LIGHT:
//                currentMainComponent.getStylesheets().remove(getClass().getResource("themes/LightMode.css").toExternalForm());
//                break;
//        }
//
//        appMode = AppMode.DEFAULT;
//    }

}
