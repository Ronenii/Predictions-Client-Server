package gui.app.menu.execution;

import gui.app.menu.MenuComponentController;
import gui.app.menu.execution.details.ExecutionDetailsComponentController;
import gui.app.menu.execution.queue.ExecutionQueueComponentController;
import gui.app.menu.execution.result.ResultTabComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ExecutionComponentController{
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

//    @Override
//    public BarNotifier getNotificationBar() {
//        return mainController.getNotificationBar();
//    }
//
//    @Override
//    public EngineAgent getEngineAgent() {
//        return mainController.getEngineAgent();
//    }
//
//    /**
//     * Adds the given simulation to the execution queue
//     */
//    public void addSimulationToQueue(SimulationRunData simulationRunData) {
//        executionQueueComponentController.addSimulationToQueue(simulationRunData);
//    }
//
//    /**
//     * Using the simulation ID of the current selected item in the table view, returns the simulationRunData.
//     */
//    public SimulationRunData getCurrentSelectedSimulation() {
//        StatusData selected = executionQueueComponentController.getQueueSelectedItem();
//        if (selected != null) {
//            return getEngineAgent().getRunDataById(selected.getSimId());
//        } else {
//            return null;
//        }
//    }
//
//    public void updateGuiToChosenSimulation(SimulationRunData simulationRunData){
//        executionDetailsComponentController.updateToChosenSimulation(simulationRunData);
//        resultTabComponentController.updateToChosenSimulation(simulationRunData);
//    }
//
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








