package gui.result.details.control.bar;

import gui.result.details.ExecutionDetailsComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ExecutionDetailsControlBarController {

    private ExecutionDetailsComponentController mainController;
    @FXML
    private Button playBTN;

    @FXML
    private Button pauseBTN;

    @FXML
    private Button stopBTN;

    public void setMainController(ExecutionDetailsComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void pauseButtonActionListener(ActionEvent event) {
    }

    @FXML
    void playButtonActionListener(ActionEvent event) {
    }

    @FXML
    void stopButtonActionListener(ActionEvent event) {
    }

}