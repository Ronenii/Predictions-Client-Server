package gui.execution.control.bar;

import engine2ui.simulation.execution.StartResponse;
import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.execution.NewExecutionComponentController;
import gui.execution.models.StartDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import manager.EngineAgent;

public class ControlBarController implements BarNotifier, EngineCommunicator {
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
            mainController.updateStartDetailsMap(response.getSimulationRunData().getSimId());
            showNotification(response.getMessage());
            if(response.isSuccess()){
                mainController.getMenusTabPane().getSelectionModel().selectLast();
                mainController.addSimulationToQueue(response.getSimulationRunData());
            }
        }
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @Override
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }
}