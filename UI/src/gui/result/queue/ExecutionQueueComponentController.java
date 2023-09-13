package gui.result.queue;

import engine2ui.simulation.runtime.SimulationRunData;
import gui.api.EngineCommunicator;
import gui.result.ResultComponentController;
import gui.result.models.StatusData;
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

    public void executeFetchingTask(String simId){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                SimulationRunData selectedInThread;
                do {
                    selectedInThread = getEngineAgent().getRunDataById(simId);// Get the most current run data from the engine
                    mainController.updateSimulationRunDataMap(simId, selectedInThread); // Update the runData references we keep in the gui
                    mainController.updateGuiToChosenSimulation(selectedInThread); // Update the components displaying the simulation
                    Thread.sleep(200); // Make the thread sleep for 200ms
                }while(selectedInThread != null && selectedInThread.getSimId().equals(simId) && !selectedInThread.isCompleted());
                return null;
            }
        };

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
