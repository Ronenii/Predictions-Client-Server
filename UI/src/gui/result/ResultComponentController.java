package gui.result;

import engine2ui.simulation.runtime.SimulationRunData;
import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.result.details.ExecutionDetailsComponentController;
import gui.result.models.StatusData;
import gui.result.queue.ExecutionQueueComponentController;
import gui.result.tab.ResultTabComponentController;
import gui.sub.menus.SubMenusController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import manager.EngineAgent;

import java.util.HashMap;
import java.util.Map;

public class ResultComponentController implements EngineCommunicator, BarNotifier {
    private SubMenusController mainController;

    @FXML
    private VBox resultTabComponent;
    @FXML
    private ResultTabComponentController resultTabComponentController;

    @FXML
    private VBox executionQueueComponent;
    @FXML
    private ExecutionQueueComponentController executionQueueComponentController;

    @FXML
    private GridPane executionDetailsComponent;
    @FXML
    private ExecutionDetailsComponentController executionDetailsComponentController;

    Map<String, SimulationRunData> simulationRunDataMap; // used to access simulation run data with the simulation ID.


    public void setMainController(SubMenusController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if (executionDetailsComponentController != null && resultTabComponentController != null && executionQueueComponentController != null) {
            executionDetailsComponentController.setMainController(this);
            resultTabComponentController.setMainController(this);
            executionQueueComponentController.setMainController(this);
        }
        simulationRunDataMap = new HashMap<>();
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @Override
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData) {
        executionQueueComponentController.addSimulationToQueue(simulationRunData);
        simulationRunDataMap.put(simulationRunData.getSimId(), simulationRunData);
    }

    /**
     * Using the simulation ID of the current selected item in the table view, returns the simulationRunData.
     */
    public SimulationRunData getCurrentSelectedSimulation() {
        StatusData selected = executionQueueComponentController.getQueueSelectedItem();
        if (selected != null) {
            return simulationRunDataMap.get(selected.getSimId());
        } else {
            return null;
        }
    }

    public void updateToChosenSimulation(SimulationRunData simulationRunData){
        executionDetailsComponentController.updateToChosenSimulation(simulationRunData);
        resultTabComponentController.updateToChosenSimulation(simulationRunData);
    }




}









