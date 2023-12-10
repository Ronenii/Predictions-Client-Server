package gui.app.menu.result;

import gui.api.Controller;
import gui.app.menu.request.data.RequestData;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.menu.result.details.ExecutionDetailsComponentController;
import gui.app.menu.result.models.StatusData;
import gui.app.menu.result.queue.ExecutionQueueComponentController;
import gui.app.menu.result.tab.ResultTabComponentController;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.app.menu.MenuComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import jaxb.event.FileLoadedEvent;
import manager.UserServerAgent;

public class ResultComponentController implements UserEngineCommunicator, Controller, FileLoadedEvent {
    private MenuComponentController mainController;

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

    public void setMainController(MenuComponentController mainController) {
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
    public UserServerAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    /**
     * Adds the given simulation to the execution queue
     */
    public void addSimulationToQueue(SimulationRunData simulationRunData, RequestData requestData) {
        executionQueueComponentController.addSimulationToQueue(simulationRunData, requestData);
    }

    /**
     * Using the simulation ID of the current selected item in the table view, returns the simulationRunData.
     */
    public void getCurrentSelectedSimulation() {
        StatusData selected = executionQueueComponentController.getQueueSelectedItem();
        if (selected != null) {
            UserServerAgent.getSimRunDataForTvMouseClick(executionQueueComponentController, selected.getSimId());
        } else {
            executionQueueComponentController.onMouseClickedTvReceiveRunData(null);
        }
    }

    public void updateGuiToChosenSimulation(SimulationRunData simulationRunData){
        executionDetailsComponentController.updateToChosenSimulation(simulationRunData);
        resultTabComponentController.updateToChosenSimulation(simulationRunData);
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


    public TabPane getMenusTabPane() {
        return mainController.getMenusTabPane();
    }

    public void setExecutionQueueTaskOnSkipForward() {
        executionQueueComponentController.setExecutionQueueTaskOnSkipForward();
    }

    public void setExecutionQueueTaskOnPause() {
        executionQueueComponentController.setExecutionQueueTaskOnPause();
    }

    public void disableExecutionQueueTaskOnPause() {
        executionQueueComponentController.disableExecutionQueueTaskOnPause();
    }

    public void setOneUpdateAfterPauseFlag() {
        executionQueueComponentController.setOneUpdateAfterPauseFlag();
    }

    public void refreshRequestsTv(){
        mainController.refreshRequestsTv();
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}









