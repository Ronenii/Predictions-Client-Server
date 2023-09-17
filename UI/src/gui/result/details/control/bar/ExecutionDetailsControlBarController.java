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

    @FXML
    private Button skipForwardBTN;

    public void setMainController(ExecutionDetailsComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void pauseButtonActionListener(ActionEvent event) {
        mainController.sendPauseToTheEngine();
        mainController.setExecutionQueueTaskOnPause();
        skipForwardBTN.setDisable(false);
    }

    @FXML
    void playButtonActionListener(ActionEvent event) {
        mainController.sendPlayToTheEngine();
        mainController.disableExecutionQueueTaskOnPause();
        skipForwardBTN.setDisable(true);
    }

    @FXML
    void stopButtonActionListener(ActionEvent event) {
        mainController.sendStopToTheEngine();
        skipForwardBTN.setDisable(true);
    }

    @FXML
    void skipForwardButtonActionListener(ActionEvent event) {
        mainController.sendSkipForwardToTheEngine();
        mainController.setExecutionQueueTaskOnSkipForward();
    }
}