package gui.app.api;

import javafx.scene.control.Alert;

/**
 * An interface that aggregates all controllers.
 */
public interface Controller {
    void showMessageInNotificationBar(String message);
    default void showAlertAndWait(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    default void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    // void setMainController(Controller controller);
}
