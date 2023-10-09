package gui.app.menu.execution.control.bar;

import server2client.simulation.execution.StartResponse;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.app.menu.execution.NewExecutionComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import manager.UserServerAgent;

public class ControlBarController implements BarNotifier, UserEngineCommunicator {
    private NewExecutionComponentController mainController;
    @FXML
    private Button startBTN;

    @FXML
    private Button clearBTN;

    public void setMainController(NewExecutionComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void clearButtonActionListener(ActionEvent event) {
        if(getEngineAgent().isFileLoaded()){
            mainController.clearInputs();
            getNotificationBar().showNotification("Cleared all inputs from user.");
        }
    }

    @FXML
    void startButtonActionListener(ActionEvent event) {
        if(getEngineAgent().isFileLoaded())
        {
            StartResponse response = getEngineAgent().startSimulation();
            if(response.isSuccess()){
                mainController.updateStartDetailsMap(response.getSimulationRunData().getSimId(), response.getSimulationRunData().getEnvVarsValuesMap());
                mainController.getMenusTabPane().getSelectionModel().selectLast();
                mainController.addSimulationToQueue(response.getSimulationRunData());
            }
            showNotification(response.getMessage());

        }
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @Override
    public UserServerAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }
}