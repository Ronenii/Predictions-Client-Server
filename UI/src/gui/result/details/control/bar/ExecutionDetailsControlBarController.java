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
            // For the first click on pause we allow the task in the execution queue controller (which fetches the runtime & result details components)
            // to complete another loop, and then we put this loop on hold.
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
            // For the first click on skip forward we create the task in the execution details component (which informs the engine moving one tick forward),
            // and after one update of the engine, the task will be put on hold until another click on the
            // skip forward button (which will go to the 'else' and allow the task run one loop).
            mainController.sendSkipForwardToTheEngine();
        } else {
            mainController.setSkipOne(true);
        }
    }
}