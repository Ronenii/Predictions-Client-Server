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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import simulation.objects.world.status.SimulationStatus;

import java.util.List;


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
    @FXML
    private VBox execDetailsVBox;
    @FXML
    private AnchorPane controlBarAnchorPane;

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if (executionDetailsControlBarController != null) {
            executionDetailsControlBarController.setMainController(this);
        }
    }

    public void updateToChosenSimulation(SimulationRunData runData) {
        enableComponent();
        simulationIdDetLabel.setText(runData.getSimId());
        currentTickDetLabel.setText(runData.getCurrentTick().getValue());
        durationDetLabel.setText(runData.getCurrentElapsedTime().getValue());
        updateEntitiesTV(runData.getEntities());
        setListeners(runData);
    }

    private void updateEntitiesTV(List<DTOEntity> dtoEntities) {
        ObservableList<PopulationData> populationList = FXCollections.observableArrayList();

        for (DTOEntity e : dtoEntities
        ) {
            populationList.add(new PopulationData(e.getName(), e.instanceQuantityProperty()));
        }

        entitiesTV.setItems(populationList);
    }

    public void enableComponent() {
        execDetailsVBox.disableProperty().set(false);
        entitiesTV.disableProperty().set(false);
    }

    public void disableComponent() {
        execDetailsVBox.disableProperty().set(true);
        entitiesTV.disableProperty().set(true);
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

        runData.getStatus().addListener((observable -> {
            if (mainController.getCurrentSelectedSimulation().equals(runData)) {
                switch (SimulationStatus.valueOf(observable.toString().toUpperCase())){
                    case WAITING:
                    case COMPLETED:
                        controlBarAnchorPane.disableProperty().set(false);
                        break;
                    case ONGOING:
                        controlBarAnchorPane.disableProperty().set(true);
                        break;
                }
            }
        }));
    }
}
