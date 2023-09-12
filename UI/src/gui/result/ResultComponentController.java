package gui.result;

import engine2ui.simulation.runtime.SimulationRunData;
import engine2ui.simulation.start.SimulationStartData;
import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.result.execution.details.ExecutionDetailsComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;
import manager.event.SimulationUpdatedEvent;

public class ResultComponentController implements EngineCommunicator, BarNotifier, SimulationUpdatedEvent {
    private SubMenusController mainController;
    @FXML
    private Label exeListLabel;
    @FXML
    private TableView<SimulationStartData> executionsQueueTV;
    @FXML
    private GridPane executionDetailsComponent;
    @FXML
    private ExecutionDetailsComponentController executionDetailsComponentController;

    public void setMainController(SubMenusController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(executionDetailsComponentController != null) {
            executionDetailsComponentController.setMainController(this);
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

    @Override
    public void onSimulationUpdated(SimulationRunData runData) {
        executionDetailsComponentController.onSimulationUpdated(runData);
    }

    public void addSimulationToQueue(SimulationStartData simStartData){
        executionsQueueTV.getItems().add(simStartData);
    }
}









