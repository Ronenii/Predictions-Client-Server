package gui.app.menu.management.thread;

import gui.app.api.Controller;
import gui.app.menu.management.ManagementComponentController;
import gui.app.menu.management.thread.refresher.ThreadDataRefresher;
import manager.constant.Constants;
import server2client.simulation.thread.data.ThreadData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import manager.AdminServerAgent;

import java.util.Timer;

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

    private ThreadDataRefresher threadDataRefresher = null;

    private Timer timer;

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

    public void startThreadDataRefresher() {
        if(threadDataRefresher == null) {
            threadDataRefresher = new ThreadDataRefresher(this);
            timer = new Timer();
            timer.schedule(threadDataRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
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
        lblQueue.setText(String.valueOf(queueManagementData.getQueueCount()));
        lblRunning.setText(String.valueOf(queueManagementData.getRunningCount()));
        lblEnded.setText(String.valueOf(queueManagementData.getCompletedCount()));
    }

    public void turnOnThreadDataRefresherFromAdminLoadDetails() {
        startThreadDataRefresher();
        isThreadPoolSet = true;
    }

    public void clearComponent() {
        lblQueue.setText("0");
        lblRunning.setText("0");
        lblEnded.setText("0");
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
