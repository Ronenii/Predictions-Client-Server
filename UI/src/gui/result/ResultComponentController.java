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
import manager.event.SimulationUpdatedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class ResultComponentController implements EngineCommunicator, BarNotifier, SimulationUpdatedEvent {
    private SubMenusController mainController;
    @FXML
    private Label exeListLabel;
    @FXML
    private TableView<StatusData> executionsQueueTV;
    @FXML
    private GridPane executionDetailsComponent;
    @FXML
    private ExecutionDetailsComponentController executionDetailsComponentController;
    Map<String, SimulationRunData> simulationRunDataMap;

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

    @Override
    public void onSimulationUpdated(SimulationRunData runData) {
        executionDetailsComponentController.onSimulationUpdated(runData);
        executionsQueueTV.refresh();
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData) {
        executionsQueueTV.getItems().add(new StatusData(simulationRunData.getSimId(), simulationRunData.status));
        simulationRunDataMap.put(simulationRunData.getSimId(), simulationRunData);
    }

    public SimulationRunData getCurrentSelectedSimulation() {
        StatusData selected = executionsQueueTV.getSelectionModel().getSelectedItem();
        if (selected != null) {
            return simulationRunDataMap.get(selected.getSimId());
        } else {
            return null;
        }
    }

    @FXML
    private void onMouseClickedTV(ActionEvent event) {
        SimulationRunData selected = getCurrentSelectedSimulation();
        if (selected != null) {
            executionDetailsComponentController.updateToChosenSimulation(selected);
        }
    }
}









