package gui.error.popup;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PopupErrorWindowController {

    @FXML
    private TextArea taErrorDisplay;

    @FXML
    public void initialize(String text) {
        taErrorDisplay.setText(text);
    }

}