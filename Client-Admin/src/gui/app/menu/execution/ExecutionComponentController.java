package gui.app.menu.execution;

import gui.app.api.Controller;
import gui.app.menu.MenuComponentController;
import server2client.simulation.status.StatusData;
import gui.app.menu.execution.details.ExecutionDetailsComponentController;
import gui.app.menu.execution.queue.ExecutionQueueComponentController;
import gui.app.menu.execution.result.ResultTabComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import manager.AdminServerAgent;
import server2client.simulation.runtime.SimulationRunData;

public class ExecutionComponentController implements Controller {
    private Controller mainController;

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

    public void setMainController(MenuComponentController controller) {
        this.mainController = controller;
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
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }

//    @Override
//    public BarNotifier getNotificationBar() {
//        return mainController.getNotificationBar();
//    }
//
//
//    /**
//     * Adds the given simulation to the execution queue
//     */
//    public void addSimulationToQueue(SimulationRunData simulationRunData) {
//        executionQueueComponentController.addSimulationToQueue(simulationRunData);
//    }
//
    /**
     * Using the simulation ID of the current selected item in the table view, returns and displays the simulationRunData.
     */
    public void getCurrentSelectedSimulation() {
        StatusData selected = executionQueueComponentController.getQueueSelectedItem();
        if (selected != null) {
            AdminServerAgent.getSimRunDataForTvMouseClick(executionQueueComponentController, selected.getSimId());
        } else {
            executionQueueComponentController.onMouseClickedTvReceiveRunData(null);
        }
    }

    public void updateGuiToChosenSimulation(SimulationRunData simulationRunData){
        executionDetailsComponentController.updateToChosenSimulation(simulationRunData);
        resultTabComponentController.updateToChosenSimulation(simulationRunData);
    }


//    public void updateQueueLblInQueueManagement() {
//        mainController.updateQueueLblInQueueManagement();
//    }
//
//    public void updateRunningAndCompletedLblsInQueueManagement(QueueManagementData queueManagementData) {
//        mainController.updateRunningAndCompletedLblsInQueueManagement(queueManagementData);
//    }
//
//    public int getSimulationCurrentTicks() {
//        return executionDetailsComponentController.getSimulationCurrentTicks();
//    }
//
//    @Override
//    public void onFileLoaded(PreviewData previewData, boolean isFirstSimulationLoaded) {
//        if(!isFirstSimulationLoaded){
//            resultTabComponentController.clearComponent();
//            resultTabComponentController.disableResultComponent();
//            executionQueueComponentController.clearComponent();
//            executionDetailsComponentController.clearComponent();
//
//        }
//    }

//    public boolean isBonusSelected(){
//        return mainController.isBonusSelected();
//    }
//
//    public void rerunSimulationById(String simId) {
//        mainController.rerunSimulationById(simId);
//    }
//
//    public TabPane getMenusTabPane() {
//        return mainController.getMenusTabPane();
//    }
//
//    public void setExecutionQueueTaskOnSkipForward() {
//        executionQueueComponentController.setExecutionQueueTaskOnSkipForward();
//    }
//
//    public void setExecutionQueueTaskOnPause() {
//        executionQueueComponentController.setExecutionQueueTaskOnPause();
//    }
//
//    public void disableExecutionQueueTaskOnPause() {
//        executionQueueComponentController.disableExecutionQueueTaskOnPause();
//    }
//
//    public void setOneUpdateAfterPauseFlag() {
//        executionQueueComponentController.setOneUpdateAfterPauseFlag();
//    }

}









