package gui.app.menu.result.queue;

import gui.api.Controller;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.menu.result.models.QueueManagementData;
import gui.app.menu.result.models.StatusData;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.app.menu.result.ResultComponentController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import manager.UserServerAgent;
import simulation.objects.world.status.SimulationStatus;

import java.util.HashMap;
import java.util.Map;

public class ExecutionQueueComponentController implements UserEngineCommunicator, Controller {
    private ResultComponentController mainController;
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


    @FXML
    public void initialize() {
        simIdCol.setCellValueFactory(new PropertyValueFactory<>("simId"));
        simStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        simulationStatusMap = new HashMap<>();
        isFetchStatusTaskRunning = false;
        resetFlags();
    }

    /**
     * If the selected simulation in the TableView is ongoing or waiting then will create a task that updates the chosen simulation every 200ms.
     * Else will just display the selected simulation.
     */
    @FXML
    void onMouseClickedTV(MouseEvent event) {
        SimulationRunData selected = mainController.getCurrentSelectedSimulation();
        resetFlags();
        if (selected != null) {
            if (selected.isCompleted()) {
                mainController.updateGuiToChosenSimulation(selected);
            } else {
                executeSimDataFetchingTask(selected.getSimId());
            }
        }
    }

    private void resetFlags() {
        isSimulationPaused = false;
        isSimulationSkippedForward = false;
        oneUpdateAfterPauseFlag = false;
    }

    /**
     * This task will fetch simulation run data from the gui and display it as long as the simulation is still selected in the TableView
     * and is ongoing.
     * Every 200ms the task will query the engine for the run data, and will update the SimulationRunDataMap in the parent accordingly.
     */
    public void executeSimDataFetchingTask(String simId) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                SimulationRunData selectedInThread;
                do {
                    selectedInThread = getEngineAgent().getRunDataById(getQueueSelectedItem().getSimId());// Get the most current run data from the engine
                    // Wrap UI updates in Platform.runLater to execute them on the FX application thread

                    // isSimulationPaused - true when the simulation on pause
                    // isSimulationSkippedForward - the skip forward button set this flag to true, allowing the task run this loop once and then return to a hold state.
                    // oneUpdateAfterPauseFlag - the pause button set this flag to true, allowing the task run this loop once (to fetch the details) and then return to a hold state.
                    if(!isSimulationPaused || isSimulationSkippedForward || oneUpdateAfterPauseFlag) {
                        // Check if we skipped forward and didn't get entities in the ResultData to load.
                        if(!isSimulationSkippedForward || selectedInThread.resultData.getEntities() != null) {
                            SimulationRunData finalSelectedInThread = selectedInThread;
                            Platform.runLater(() -> {
                                mainController.updateGuiToChosenSimulation(finalSelectedInThread); // Update the components displaying the simulation
                            });
                            isSimulationSkippedForward = false;
                        }
                        oneUpdateAfterPauseFlag = false;
                    }

                    Thread.sleep(200); // Make the thread sleep for 200ms
                } while (selectedInThread != null && selectedInThread.getSimId().equals(simId) && !selectedInThread.isCompleted());
                return null;
            }
        };

        runTask(task);
    }

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData) {
        StatusData toAdd = new StatusData(simulationRunData.getSimId(), new SimpleStringProperty(simulationRunData.getStatus()));
        simulationStatusMap.put(toAdd, SimulationStatus.valueOf(toAdd.getStatus()));
        executionsQueueTV.getItems().add(toAdd);
        executeSimStatusFetchingTask();
    }

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
        QueueManagementData queueManagementData = new QueueManagementData();

        for (StatusData s : simulationStatusMap.keySet()) {
            updateQueueManagementData(queueManagementData, s.getStatus());
            if (!s.getStatus().equals(SimulationStatus.COMPLETED.name())) {
                UserServerAgent.getSimRunDataForSimStatus(this, s);
            }
        }
    }

    public void statusUpdateForSingleRunningSimulation(SimulationRunData selectedInThread, StatusData statusData) {
        if(selectedInThread.errorMessage != null){
            Platform.runLater(() -> {
                showMessageInNotificationBar(selectedInThread.errorMessage);
            });
        }

        showNotificationIfSimulationRunStarted(statusData, selectedInThread);
        Platform.runLater(() -> {
            statusData.statusProperty().set(selectedInThread.getStatus());
            showNotificationIfSimulationRunCompleted(selectedInThread);
        });
    }


    /**
     * Fetch the 'QueueManagementData' object according to the current simulations in the queue.
     */
    private void updateQueueManagementData(QueueManagementData queueManagementData, String status) {
        if(status.equals("ONGOING")) {
            queueManagementData.runningCount++;
        } else if (status.equals("COMPLETED")) {
            queueManagementData.completedCount++;
        }
    }

    /**
     * Will return true if the simulation is about to be started.
     */
    private boolean isStartedSimulation(StatusData current, SimulationRunData next) {
        return SimulationStatus.valueOf(current.getStatus()) == SimulationStatus.WAITING && SimulationStatus.valueOf(next.status) == SimulationStatus.ONGOING;
    }

    /**
     * Prints a notification if the simulation is about to start.
     */
    private void showNotificationIfSimulationRunStarted(StatusData statusData, SimulationRunData simulationRunData) {
        if (isStartedSimulation(statusData, simulationRunData)) {
            Platform.runLater(() -> {
                showMessageInNotificationBar(String.format("Simulation %s has started it's run.", statusData.getSimId()));
            });
        }
    }

    /**
     * Will only run inside platform run later since it happens only after a simulation's status has changed to COMPLETED.
     */
    private void showNotificationIfSimulationRunCompleted(SimulationRunData simulationRunData) {
        if (SimulationStatus.valueOf(simulationRunData.getStatus()) == SimulationStatus.COMPLETED) {
           showMessageInNotificationBar(String.format("Simulation %s has finished it's run.", simulationRunData.getSimId()));
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

    @Override
    public synchronized UserServerAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public void clearComponent() {
        executionsQueueTV.getItems().clear();
        simIdCol.setCellValueFactory(new PropertyValueFactory<>("simId"));
        simStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        simulationStatusMap.clear();
        isFetchStatusTaskRunning = false;
    }

    public void setExecutionQueueTaskOnSkipForward() {
        isSimulationSkippedForward = true;
    }

    public void setExecutionQueueTaskOnPause() {
        isSimulationPaused = true;
    }

    public void disableExecutionQueueTaskOnPause() {
        isSimulationPaused = false;
    }

    public void setOneUpdateAfterPauseFlag() {
        oneUpdateAfterPauseFlag = true;
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
