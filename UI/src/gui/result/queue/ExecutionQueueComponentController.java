package gui.result.queue;

import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.models.StatusData;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ExecutionQueueComponentController {
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
            mainController.updateToChosenSimulation(selected);

        }
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
}
