package gui.app.menu.management.thread;

import gui.app.api.Controller;
import gui.app.menu.management.ManagementComponentController;
import gui.app.menu.management.thread.data.ThreadData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import manager.AdminServerAgent;

public class ThreadManagerComponentController implements Controller {
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

    private boolean isThreadPoolSet = false;

    private int queueCount = 0;

    public void setMainController(ManagementComponentController managementComponentController){
        this.mainController = managementComponentController;
    }

    @FXML
    void onButtonSetThreadCountClicked(ActionEvent event) {
        try {
            int threadCount = Integer.parseInt(textFieldThreadCount.getText());

            AdminServerAgent.sendSimulationThreadCount(this, threadCount);
        } catch (NumberFormatException e) {
            showMessageInNotificationBar("The thread count needs to be a number!");
        }
    }

    public void setThreadPoolSet() {
        isThreadPoolSet = true;
    }

    public boolean isThreadPoolSet() {
        return isThreadPoolSet;
    }

    @FXML
    void onTextFieldThreadCountAction(ActionEvent event) {
        onButtonSetThreadCountClicked(event);
    }

    public void enableThreadComponent() {
        textFieldThreadCount.setDisable(false);
        buttonSetThreadCount.setDisable(false);
    }

    public void updateLabelsInQueueManagement(ThreadData queueManagementData) {
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

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
