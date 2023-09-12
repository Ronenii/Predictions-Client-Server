package gui.result.execution.details;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.execution.details.control.bar.ExecutionDetailsControlBarController;
import gui.result.models.PopulationData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class ExecutionDetailsComponentController {
    private ResultComponentController mainController;
    @FXML
    private TableView<PopulationData> entitiesTV;

    @FXML
    private Label executionDetLabel;

    @FXML
    private Label simulationIdDetLabel;

    @FXML
    private Label currentTickDetLabel;

    @FXML
    private Label durationDetLabel;
    @FXML
    private GridPane executionDetailsControlBar;
    @FXML
    private ExecutionDetailsControlBarController executionDetailsControlBarController;

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if (executionDetailsControlBarController != null) {
            executionDetailsControlBarController.setMainController(this);
        }
    }

    public void updateToChosenSimulation(SimulationRunData runData){
        simulationIdDetLabel.setText(runData.getSimId());
        currentTickDetLabel.setText(runData.getCurrentTick().getValue());
        durationDetLabel.setText(runData.getCurrentElapsedTime().getValue());
        updateEntitiesTV(runData.getEntities());
        setListeners(runData);
    }

    private void updateEntitiesTV(List<DTOEntity> dtoEntities){
        ObservableList<PopulationData> populationList = FXCollections.observableArrayList();

        for (DTOEntity e: dtoEntities
             ) {
            populationList.add(new PopulationData(e.getName(), e.instanceQuantityProperty()));
        }

        entitiesTV.setItems(populationList);
    }

    public void setListeners(SimulationRunData runData) {
        runData.getCurrentElapsedTime().addListener((observable, oldValue, newValue) -> {
            if (mainController.getCurrentSelectedSimulation().equals(runData)) {
                currentTickDetLabel.setText(newValue);
            }
        });
        runData.getCurrentTick().addListener(((observable, oldValue, newValue) -> {
            if (mainController.getCurrentSelectedSimulation().equals(runData)) {
                durationDetLabel.setText(newValue);
            }
        }));
        for (DTOEntity e : runData.getEntities()
        ) {
            e.instanceQuantityProperty().addListener(((observable, oldValue, newValue) -> {
                if (mainController.getCurrentSelectedSimulation().equals(runData)) {
                    entitiesTV.refresh();
                }
            }));
        }
    }
}
