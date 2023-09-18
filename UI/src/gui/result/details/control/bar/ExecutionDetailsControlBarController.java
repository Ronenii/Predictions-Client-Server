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

    private boolean isSkipForwardFirstClick = true;

    private boolean isPauseFirstClick = true;

    public void setMainController(ExecutionDetailsComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void pauseButtonActionListener(ActionEvent event) {
        mainController.sendPauseToTheEngine();
        mainController.setExecutionQueueTaskOnPause();
        if(isPauseFirstClick){
            isPauseFirstClick = false;
            mainController.setOneUpdateAfterPauseFlag();
        }
        skipForwardBTN.setDisable(false);
    }

    @FXML
    void playButtonActionListener(ActionEvent event) {
        mainController.sendPlayToTheEngine();
        mainController.disableExecutionQueueTaskOnPause();
        mainController.setPlayButtonClicked(true);
        isSkipForwardFirstClick = true;
        isPauseFirstClick = true;
        skipForwardBTN.setDisable(true);
    }

    @FXML
    void stopButtonActionListener(ActionEvent event) {
        mainController.sendStopToTheEngine();
        mainController.disableExecutionQueueTaskOnPause();
        isSkipForwardFirstClick = true;
        isPauseFirstClick = true;
        skipForwardBTN.setDisable(true);
    }

    @FXML
    void skipForwardButtonActionListener(ActionEvent event) {
        if(isSkipForwardFirstClick){
            isSkipForwardFirstClick = false;
            mainController.sendSkipForwardToTheEngine();
        } else {
            mainController.setSkipOne(true);
        }
    }
}