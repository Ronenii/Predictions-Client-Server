package gui.app.menu.management.thread;

import gui.app.menu.management.ManagementComponentController;
import gui.app.menu.management.thread.data.QueueManagementData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ThreadManagerComponentController {
    private ManagementComponentController mainController;

    @FXML
    private TextField textFieldThreadCount;

    @FXML
    private Button buttonSetThreadCount;

    @FXML
    private Label lblQueue;

    @FXML
    private Label lblRunning;

    @FXML
    private Label lblEnded;

    private int queueCount = 0;

    public void setMainController(ManagementComponentController managementComponentController){
        this.mainController = managementComponentController;
    }

    @FXML
    void onButtonSetThreadCountClicked(ActionEvent event) {

    }

    @FXML
    void onTextFieldThreadCountAction(ActionEvent event) {

    }

    public void updateLabelsInQueueManagement(QueueManagementData queueManagementData) {
        lblRunning.setText(String.valueOf(queueManagementData.runningCount));
        lblEnded.setText(String.valueOf(queueManagementData.completedCount));
    }

    public void incrementQueue() {
        queueCount++;
        lblQueue.setText(String.valueOf(queueCount));
    }

    public void clearComponent() {
        queueCount = 0;
        lblQueue.setText("0");
        lblRunning.setText("0");
        lblEnded.setText("0");
    }
}
