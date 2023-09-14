package gui.result.queue;

import engine2ui.simulation.runtime.SimulationRunData;
import gui.api.EngineCommunicator;
import gui.result.ResultComponentController;
import gui.result.models.StatusData;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import manager.EngineAgent;

public class ExecutionQueueComponentController implements EngineCommunicator {
    private ResultComponentController mainController;
    @FXML
    private Label exeListLabel;

    @FXML
    private TableView<StatusData> executionsQueueTV;

    @FXML
    private TableColumn<StatusData, String> simIdCol;
    @FXML
    private TableColumn<StatusData, String> simStatusCol;

    @FXML
    public void initialize() {
        simIdCol.setCellValueFactory(new PropertyValueFactory<>("simId"));
        simStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * If the selected simulation in the TableView is ongoing or waiting then will create a task that updates the chosen simulation every 200ms.
     * Else will just display the selected simulation.
     *
     */
    @FXML
    void onMouseClickedTV(MouseEvent event) {
        SimulationRunData selected = mainController.getCurrentSelectedSimulation();
        if (selected != null) {
            if(selected.isCompleted()){
                mainController.updateGuiToChosenSimulation(selected);
            }
            else{
                executeFetchingTask(selected.getSimId());
            }
        }
    }

    /**
     * This task will fetch simulation run data from the gui and display it as long as the simulation is still selected in the TableView
     * and is ongoing.
     * Every 200ms the task will query the engine for the run data, and will update the SimulationRunDataMap in the parent accordingly.
     */
    public void executeFetchingTask(String simId){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                SimulationRunData selectedInThread;
                do {
                    selectedInThread = getEngineAgent().getRunDataById(getQueueSelectedItem().getSimId());// Get the most current run data from the engine

                    // Wrap UI updates in Platform.runLater to execute them on the FX application thread
                    SimulationRunData finalSelectedInThread = selectedInThread;
                    Platform.runLater(() -> {
                        mainController.updateGuiToChosenSimulation(finalSelectedInThread); // Update the components displaying the simulation
                    });

                    Thread.sleep(200); // Make the thread sleep for 200ms
                } while (selectedInThread != null && selectedInThread.getSimId().equals(simId) && !selectedInThread.isCompleted());
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true); // Mark the thread as a daemon to allow application exit
        thread.start();
    }

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData){
        executionsQueueTV.getItems().add(new StatusData(simulationRunData.getSimId(), new SimpleStringProperty(simulationRunData.getStatus())));
        executionsQueueTV.refresh();
    }

    public StatusData getQueueSelectedItem(){
        return executionsQueueTV.getSelectionModel().getSelectedItem();
    }

    @Override
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }
}
