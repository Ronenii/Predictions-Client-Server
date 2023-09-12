package gui.result;

import engine2ui.simulation.runtime.SimulationRunData;
import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.result.execution.details.ExecutionDetailsComponentController;
import gui.result.models.StatusData;
import gui.sub.menus.SubMenusController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;
import java.util.HashMap;
import java.util.Map;

public class ResultComponentController implements EngineCommunicator, BarNotifier {
    private SubMenusController mainController;
    @FXML
    private Label exeListLabel;
    @FXML
    private TableView<StatusData> executionsQueueTV;
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
        if (executionDetailsComponentController != null) {
            executionDetailsComponentController.setMainController(this);
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
        executionsQueueTV.getItems().add(new StatusData(simulationRunData.getSimId(), simulationRunData.status));
        simulationRunDataMap.put(simulationRunData.getSimId(), simulationRunData);
    }

    /**
     * Using the simulation ID of the current selected item in the table view, returns the simulationRunData.
     */
    public SimulationRunData getCurrentSelectedSimulation() {
        StatusData selected = executionsQueueTV.getSelectionModel().getSelectedItem();
        if (selected != null) {
            return simulationRunDataMap.get(selected.getSimId());
        } else {
            return null;
        }
    }

    /**
     * Updates the execution component based on the currently selected table view component.
     */
    @FXML
    private void onMouseClickedTV(ActionEvent event) {
        SimulationRunData selected = getCurrentSelectedSimulation();
        if (selected != null) {
            executionDetailsComponentController.updateToChosenSimulation(selected);
        }
    }
}









