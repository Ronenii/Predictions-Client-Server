package gui.app.menu.execution.queue;


import gui.app.menu.execution.ExecutionComponentController;
import javafx.application.Platform;
import manager.AdminServerAgent;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.api.Controller;
import gui.app.menu.execution.queue.data.StatusData;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import simulation.objects.world.status.SimulationStatus;

import java.util.HashMap;
import java.util.Map;

public class ExecutionQueueComponentController implements Controller {
    private ExecutionComponentController mainController;
    @FXML
    private Label exeListLabel;

    @FXML
    private TableView<StatusData> executionsQueueTV;

    @FXML
    private TableColumn<StatusData, String> simIdCol;
    @FXML
    private TableColumn<StatusData, String> simStatusCol;

    private Map<StatusData, SimulationStatus> simulationStatusMap;
    private boolean isFetchStatusTaskRunning;
    private boolean isSimulationPaused;
    private boolean isSimulationSkippedForward;
    private boolean oneUpdateAfterPauseFlag;

    public void setMainController(ExecutionComponentController controller) {
        this.mainController = controller;
    }


    @FXML
    public void initialize() {
        simIdCol.setCellValueFactory(new PropertyValueFactory<>("simId"));
        simStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        simulationStatusMap = new HashMap<>();
        isFetchStatusTaskRunning = false;
        resetFlags();
    }

    private void resetFlags() {
        isSimulationPaused = false;
        isSimulationSkippedForward = false;
        oneUpdateAfterPauseFlag = false;
    }

    @FXML
    void onMouseClickedTV(MouseEvent event) {
        mainController.getCurrentSelectedSimulation();
    }


    public void onMouseClickedTvReceiveRunData(SimulationRunData selected) {
        resetFlags();
        if (selected != null) {
            if (selected.isCompleted()) {
                mainController.updateGuiToChosenSimulation(selected);
            } else {
                long threadSleep = selected.getThreadSleepCount();
                if(threadSleep == 0){
                    threadSleep = 200;
                }

                executeSimDataFetchingTask(selected.getSimId(), threadSleep);
            }
        }
    }

    /**
     * This task will fetch simulation run data from the server and display it as long as the simulation is still selected in the TableView
     * and is ongoing.
     * Every 200ms the task will query the server for the run data, and will update the SimulationRunDataMap in the parent accordingly.
     * Note: the implementation uses a task instead of a refresher,
     * because the refresher relies on one thread, and in this case we may need multiple threads to run simultaneously.
     */
    public void executeSimDataFetchingTask(String simId, long threadSleep) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                do {
                    callMethodGetSimRunDataForSimProgress(this, simId);
                    Thread.sleep(threadSleep + 50); // Make the thread sleep for the exact time the thread in the server sleeps + 50ms.
                } while (true); // while true explanation on 'receiveSimulationRunForRunningSimulation' documentation.
            }
        };

        runTask(task);
    }

    /**
     * This method is needed because in the task's lambda, 'this' object does not refer to the 'ExecutionQueueComponentController' but to the calling task.
     */
    private void callMethodGetSimRunDataForSimProgress(Task<Void> task, String simId) {
        AdminServerAgent.getSimRunDataForSimProgress(this, getQueueSelectedItem().getSimId(), task, simId);// Get the most current run data from the engine
    }


//    /**
//     * This task will fetch simulation run data from the gui and display it as long as the simulation is still selected in the TableView
//     * and is ongoing.
//     * Every 200ms the task will query the engine for the run data, and will update the SimulationRunDataMap in the parent accordingly.
//     */
//    public void executeSimDataFetchingTask(String simId) {
//
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                SimulationRunData selectedInThread;
//                do {
//                    selectedInThread = getEngineAgent().getRunDataById(getQueueSelectedItem().getSimId());// Get the most current run data from the engine
//                    // Wrap UI updates in Platform.runLater to execute them on the FX application thread
//
//                    // isSimulationPaused - true when the simulation on pause
//                    // isSimulationSkippedForward - the skip forward button set this flag to true, allowing the task run this loop once and then return to a hold state.
//                    // oneUpdateAfterPauseFlag - the pause button set this flag to true, allowing the task run this loop once (to fetch the details) and then return to a hold state.
//                    if(!isSimulationPaused || isSimulationSkippedForward || oneUpdateAfterPauseFlag) {
//                        // Check if we skipped forward and didn't get entities in the ResultData to load.
//                        if(!isSimulationSkippedForward || selectedInThread.resultData.getEntities() != null) {
//                            SimulationRunData finalSelectedInThread = selectedInThread;
//                            Platform.runLater(() -> {
//                                mainController.updateGuiToChosenSimulation(finalSelectedInThread); // Update the components displaying the simulation
//                            });
//                            isSimulationSkippedForward = false;
//                        }
//                        oneUpdateAfterPauseFlag = false;
//                    }
//
//                    Thread.sleep(200); // Make the thread sleep for 200ms
//                } while (selectedInThread != null && selectedInThread.getSimId().equals(simId) && !selectedInThread.isCompleted());
//                return null;
//            }
//        };
//
//        runTask(task);
//    }

    /**
     * The task responsible for updating the status in the simulation execution queue.
     */
    private void executeSimStatusFetchingTask() {
        // Check that we don't have more than one instance of this task running.
        if (!isFetchStatusTaskRunning) {
            isFetchStatusTaskRunning = true;
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    do {
                        getStatusUpdatesForRunningSimulations();
                        Thread.sleep(200); // Make the thread sleep for 200ms
                    } while (hasNonCompletedSimulations());
                    return null;
                }
            };

            runTask(task);
        }
    }

    /**
     * Iterates on all waiting\ongoing simulations and queries the engine for updates.
     */
    private void getStatusUpdatesForRunningSimulations() {
        for (StatusData s : simulationStatusMap.keySet()) {
            if (!s.getStatus().equals(SimulationStatus.COMPLETED.name())) {
                AdminServerAgent.getSimRunDataFromSimStatus(this, s);
            }
        }
    }
//
//        // JAT will update the Queue Manager's labels.
//        Platform.runLater(() -> {mainController.updateRunningAndCompletedLblsInQueueManagement(queueManagementData);});
//    }

    /**
     * Will return true if the simulation is about to be started.
     */
    private boolean isStartedSimulation(StatusData current, SimulationRunData next) {
        return SimulationStatus.valueOf(current.getStatus()) == SimulationStatus.WAITING && SimulationStatus.valueOf(next.status) == SimulationStatus.ONGOING;
    }

//    /**
//     * Prints a notification if the simulation is about to start.
//     */
//    private void showNotificationIfSimulationRunStarted(StatusData statusData, SimulationRunData simulationRunData) {
//        if (isStartedSimulation(statusData, simulationRunData)) {
//            Platform.runLater(() -> {
//                getNotificationBar().showNotification(String.format("Simulation %s has started it's run.", statusData.getSimId()));
//            });
//        }
//    }
//
//    /**
//     * Will only run inside platform run later since it happens only after a simulation's status has changed to COMPLETED.
//     */
//    private void showNotificationIfSimulationRunCompleted(SimulationRunData simulationRunData) {
//        if (SimulationStatus.valueOf(simulationRunData.getStatus()) == SimulationStatus.COMPLETED) {
//            getNotificationBar().showNotification(String.format("Simulation %s has finished it's run.", simulationRunData.getSimId()));
//        }
//    }

    public void statusUpdateForSingleRunningSimulation(SimulationRunData selectedInThread, StatusData statusData) {
        if (selectedInThread.errorMessage != null) {
            Platform.runLater(() -> {
                showMessageInNotificationBar(selectedInThread.errorMessage);
            });
        }
    }
        /**
         * Given a task, this creates a thread for it and runs it.
         */
    private void runTask(Task<Void> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true); // Mark the thread as a daemon to allow application exit
        thread.start();
    }

    private boolean hasNonCompletedSimulations() {
        return simulationStatusMap.containsValue(SimulationStatus.ONGOING) || simulationStatusMap.containsValue(SimulationStatus.WAITING);
    }

    public StatusData getQueueSelectedItem() {
        return executionsQueueTV.getSelectionModel().getSelectedItem();
    }

    /**
     * This method called from the server's response to the 'callMethodGetSimRunDataForSimProgress' method with a SimulationRunData object.
     * This method fetch the simulation run data received from the server.
     * The method receives a reference to the task that sends the requests to the server in order to cancel this task if needed.
     */
    public void receiveSimulationRunForRunningSimulation(SimulationRunData selectedInThread, String simId, Task<Void> task) {
        // Wrap UI updates in Platform.runLater to execute them on the FX application thread
        // isSimulationPaused - true when the simulation on pause
        // isSimulationSkippedForward - the skip forward button set this flag to true, allowing the task run this loop once and then return to a hold state.
        // oneUpdateAfterPauseFlag - the pause button set this flag to true, allowing the task run this loop once (to fetch the details) and then return to a hold state.
        if(!isSimulationPaused || isSimulationSkippedForward || oneUpdateAfterPauseFlag) {
            // Check if we skipped forward and didn't get entities in the ResultData to load.
            if(!isSimulationSkippedForward || selectedInThread.resultData.getEntities() != null) {
                Platform.runLater(() -> {
                    mainController.updateGuiToChosenSimulation(selectedInThread); // Update the components displaying the simulation
                });
                isSimulationSkippedForward = false;
            }
            oneUpdateAfterPauseFlag = false;
        }

        //Check if the task loop to be canceled.
        if(selectedInThread == null || !selectedInThread.getSimId().equals(simId) || selectedInThread.isCompleted()) {
            task.cancel(true);
        }
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
