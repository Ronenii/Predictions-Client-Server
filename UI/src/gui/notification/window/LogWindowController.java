package gui.notification.window;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class LogWindowController {

    @FXML
    private TextArea taNotificationDisplay;

    @FXML
    public void initialize(String text) {
        taNotificationDisplay.setText(text);
    }

}