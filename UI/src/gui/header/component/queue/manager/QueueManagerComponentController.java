package gui.header.component.queue.manager;

import gui.header.component.HeaderComponentController;
import gui.result.models.QueueManagementData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QueueManagerComponentController {

    private HeaderComponentController mainController;
    @FXML
    private Label lblQueue;

    @FXML
    private Label lblRunning;

    @FXML
    private Label lblEnded;

    private int lblQueueCount = 0;
    private int lblRunningCount = 0;
    private int lblEndedCount = 0;

    public void setMainController(HeaderComponentController mainController) {
        this.mainController = mainController;
    }

    public void incrementLblQueue() {
        lblQueueCount++;
        lblQueue.setText(String.valueOf(lblQueueCount));
    }

    public void incrementLblRunning() {
        lblRunningCount++;
        lblRunning.setText(String.valueOf(lblRunningCount));
    }

    public void decrementLblRunning() {
        lblRunningCount--;
        lblRunning.setText(String.valueOf(lblRunningCount));
    }

    public void incrementLblEnded() {
        lblEndedCount++;
        lblEnded.setText(String.valueOf(lblEndedCount));
    }

    public void updateRunningAndCompletedLblsInQueueManagement(QueueManagementData queueManagementData) {
        lblRunning.setText(String.valueOf(queueManagementData.runningCount));
        lblEnded.setText(String.valueOf(queueManagementData.completedCount));
    }

}

