package gui.result.execution.details;

import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.execution.details.control.bar.ExecutionDetailsControlBarController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import manager.event.SimulationUpdatedEvent;

public class ExecutionDetailsComponentController implements SimulationUpdatedEvent {
    private ResultComponentController mainController;
    @FXML
    private TableView<?> entitiesTV;

    @FXML
    private Label executionDetLabel;

    @FXML
    private Label simulationIdDetLabel;

    @FXML
    private Label currentTickDetLabel;

    @FXML
    private Label durationDetLabel;
    @FXML
    private GridPane executionDetailsControlBar;
    @FXML
    private ExecutionDetailsControlBarController executionDetailsControlBarController;

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(executionDetailsControlBarController != null) {
            executionDetailsControlBarController.setMainController(this);
        }
    }

    @Override
    public void onSimulationUpdated(SimulationRunData runData) {

    }
}
