package gui.result;

import engine2ui.simulation.runtime.SimulationRunData;
import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.result.execution.details.ExecutionDetailsComponentController;
import gui.result.models.StatusData;
import gui.sub.menus.SubMenusController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
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

    @FXML
    private TabPane executionResultTP;

    @FXML
    private Label executionResultLabel;

    Map<String, SimulationRunData> simulationRunDataMap; // used to access simulation run data with the simulation ID.

    private boolean isEnabled;

    public void setMainController(SubMenusController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if (executionDetailsComponentController != null) {
            executionDetailsComponentController.setMainController(this);
        }
        simulationRunDataMap = new HashMap<>();
        isEnabled = false;
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
        if (!isEnabled) {
            enableComponent();
        }
        executionsQueueTV.getItems().add(new StatusData(simulationRunData.getSimId(), simulationRunData.status));
        simulationRunDataMap.put(simulationRunData.getSimId(), simulationRunData);
    }

    private void enableComponent() {
        isEnabled = true;
        exeListLabel.disableProperty().set(false);
        executionsQueueTV.disableProperty().set(false);
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
            if (selected.isCompleted) {
                enableResultComponent();
            } else {
                disableResultComponent();
            }
        }
    }

    private void enableResultComponent() {
        executionResultLabel.disableProperty().set(false);
        executionResultTP.disableProperty().set(false);

        loadResultComponent();
    }

    private void disableResultComponent() {
        executionResultLabel.disableProperty().set(true);
        executionResultTP.disableProperty().set(true);

        unloadResultComponent();
    }

    private void loadResultComponent(){
        unloadResultComponent();
    }

    private void unloadResultComponent(){

    }
}









