package gui.notification.error;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ErrorWindowController {

    @FXML
    private TextArea taErrorDisplay;

    @FXML
    public void initialize(String text) {
        taErrorDisplay.setText(text);
    }

}