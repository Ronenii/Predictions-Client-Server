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

    public void setMainController(HeaderComponentController mainController) {
        this.mainController = mainController;
    }

    public void incrementLblQueue() {
        lblQueueCount++;
        lblQueue.setText(String.valueOf(lblQueueCount));
    }

    public void updateRunningAndCompletedLblsInQueueManagement(QueueManagementData queueManagementData) {
        lblRunning.setText(String.valueOf(queueManagementData.runningCount));
        lblEnded.setText(String.valueOf(queueManagementData.completedCount));
    }

    public void clearComponent() {
        lblQueueCount = 0;
        lblQueue.setText("0");
        lblRunning.setText("0");
        lblEnded.setText("0");
    }

}

