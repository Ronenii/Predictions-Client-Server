package gui.app.notification.window;

import gui.app.api.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class LogWindowController implements Controller {

    @FXML
    private TextArea taNotificationDisplay;

    @FXML
    public void initialize(String text) {
        taNotificationDisplay.setText(text);
    }

    public void changeTextAreaText(String text){
        taNotificationDisplay.setText(text);
    }

    @Override
    public void showMessageInNotificationBar(String message) {
    }

    @Override
    public void setMainController(Controller controller) {
    }
}