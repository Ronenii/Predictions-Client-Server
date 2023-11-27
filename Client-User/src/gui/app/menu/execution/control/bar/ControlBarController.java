package gui.app.menu.execution.control.bar;

import gui.api.Controller;
import server2client.simulation.execution.StartResponse;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.app.menu.execution.NewExecutionComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import manager.UserServerAgent;

public class ControlBarController implements Controller, UserEngineCommunicator {
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
            showMessageInNotificationBar("Cleared all inputs from user.");
        }
    }

    @FXML
    void startButtonActionListener(ActionEvent event) {
        if(!mainController.isExecuted())
        {
            StartResponse response = getEngineAgent().startSimulation();
            if(response.isSuccess()){
                mainController.updateStartDetailsMap(response.getSimulationRunData().getSimId(), response.getSimulationRunData().getEnvVarsValuesMap());
                mainController.getMenusTabPane().getSelectionModel().selectLast();
                mainController.addSimulationToQueue(response.getSimulationRunData());
                mainController.setExecuted(true);
            }
            showMessageInNotificationBar(response.getMessage());

        } else {
            showMessageInNotificationBar("Request already started!");
        }
    }

    @Override
    public UserServerAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}