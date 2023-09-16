package gui.result;

import engine2ui.simulation.prview.PreviewData;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.result.details.ExecutionDetailsComponentController;
import gui.result.models.StatusData;
import gui.result.queue.ExecutionQueueComponentController;
import gui.result.models.QueueManagementData;
import gui.result.tab.ResultTabComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import jaxb.event.FileLoadedEvent;
import manager.EngineAgent;

public class ResultComponentController implements EngineCommunicator, BarNotifier, FileLoadedEvent {
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
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @Override
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    /**
     * Adds the given simulation to the execution queue
     */
    public void addSimulationToQueue(SimulationRunData simulationRunData) {
        executionQueueComponentController.addSimulationToQueue(simulationRunData);
    }

    /**
     * Using the simulation ID of the current selected item in the table view, returns the simulationRunData.
     */
    public SimulationRunData getCurrentSelectedSimulation() {
        StatusData selected = executionQueueComponentController.getQueueSelectedItem();
        if (selected != null) {
            return getEngineAgent().getRunDataById(selected.getSimId());
        } else {
            return null;
        }
    }

    public void updateGuiToChosenSimulation(SimulationRunData simulationRunData){
        executionDetailsComponentController.updateToChosenSimulation(simulationRunData);
        resultTabComponentController.updateToChosenSimulation(simulationRunData);
    }

    public void updateQueueLblInQueueManagement() {
        mainController.updateQueueLblInQueueManagement();
    }

    public void updateRunningAndCompletedLblsInQueueManagement(QueueManagementData queueManagementData) {
        mainController.updateRunningAndCompletedLblsInQueueManagement(queueManagementData);
    }

    public int getSimulationCurrentTicks() {
        return executionDetailsComponentController.getSimulationCurrentTicks();
    }

    @Override
    public void onFileLoaded(PreviewData previewData, boolean isFirstSimulationLoaded) {
        if(!isFirstSimulationLoaded){
            resultTabComponentController.clearComponent();
            resultTabComponentController.disableResultComponent();
            executionQueueComponentController.clearComponent();
            executionDetailsComponentController.clearComponent();

        }
    }

}









