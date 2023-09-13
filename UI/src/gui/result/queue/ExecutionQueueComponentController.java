package gui.result.queue;

import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.models.StatusData;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ExecutionQueueComponentController {
    private ResultComponentController mainController;
    @FXML
    private Label exeListLabel;

    @FXML
    private TableView<StatusData> executionsQueueTV;

    @FXML
    void onMouseClickedTV(MouseEvent event) {

    }

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData){
        executionsQueueTV.getItems().add(new StatusData(simulationRunData.getSimId(), simulationRunData.status));
    }

    public StatusData getQueueSelectedItem(){
        return executionsQueueTV.getSelectionModel().getSelectedItem();
    }
}
