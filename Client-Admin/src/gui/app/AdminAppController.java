package gui.app;

import gui.app.api.Controller;
import gui.app.menu.MenuComponentController;
import gui.app.notification.NotificationBarComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import manager.AdminServerAgent;

import java.util.EventListener;
import java.util.List;

public class AdminAppController implements Controller {
    @FXML private TabPane menuComponent;
    @FXML private Controller menuComponentController;
    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;
    @FXML private AnchorPane anchorNotification;
    public AdminServerAgent engineAgent;

    @Override
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
    }

    public void setPrimaryStageOnClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
        });
    }


    public List<EventListener> getAllFileLoadedListeners(){
        // TODO: PLACEHOLDER
        return null;
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        notificationBarComponentController.showMessageInNotificationBar(message);
    }

    /**
     * Displays an alert window for the user and pauses this JAT until the user closes the alert.
     */
    public void showAlertAndWait(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
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
