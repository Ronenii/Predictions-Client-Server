package gui.result.details;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.details.control.bar.ExecutionDetailsControlBarController;
import gui.result.models.PopulationData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import simulation.objects.world.status.SimulationStatus;

import java.util.List;


public class ExecutionDetailsComponentController {
    private ResultComponentController mainController;
    @FXML
    private TableView<PopulationData> entitiesTV;

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
    private AnchorPane controlBarAnchorPane;

    private SimpleStringProperty ticksProperty;
    private SimpleStringProperty durationProperty;
    private SimpleStringProperty simIdProperty;
    private SimpleStringProperty statusProperty;


    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if (executionDetailsControlBarController != null) {
            executionDetailsControlBarController.setMainController(this);
        }
        initProperties();
    }

    /**
     * Initializes the properties and sets a listener for each property.
     */
    private void initProperties() {
        ticksProperty = new SimpleStringProperty();
        durationProperty = new SimpleStringProperty();
        simIdProperty = new SimpleStringProperty();
        statusProperty = new SimpleStringProperty();

        // Binds the ticks display label and this property
        ticksProperty.addListener((observable, oldValue, newValue) -> {
            currentTickDetLabel.setText(newValue);
        });

        // Binds the duration display label and this property
        durationProperty.addListener(((observable, oldValue, newValue) -> {
            durationDetLabel.setText(newValue);
        }));

        // Binds the simID display label and this property
        simIdProperty.addListener(((observable, oldValue, newValue) -> {
            simulationIdDetLabel.setText(newValue);
        }));

        // Binds the enabling and disabling of the control bar to this property
        statusProperty.addListener(((observable, oldValue, newValue) -> {
            switch (SimulationStatus.valueOf(observable.toString().toUpperCase())) {
                case WAITING:
                case COMPLETED:
                    controlBarAnchorPane.disableProperty().set(false);
                    break;
                case ONGOING:
                    controlBarAnchorPane.disableProperty().set(true);
                    break;
            }
        }));
    }

    /**
     * Updates the components according to the given simulation run data.
     */
    public void updateToChosenSimulation(SimulationRunData runData) {
        clearExecutionDetails();
        ticksProperty.set(String.valueOf(runData.getTick()));
        durationProperty.set(String.valueOf(runData.getTime()));
        statusProperty.set(runData.getSimId());
        simIdProperty.set(String.valueOf(runData.getSimId()));
        updateEntitiesTV(runData.getEntities());
    }

    /**
     * @param dtoEntities The entities we want to display the quantities of in the
     *                    entitiesTV.
     */
    private void updateEntitiesTV(List<DTOEntity> dtoEntities) {
        ObservableList<PopulationData> populationList = FXCollections.observableArrayList();

        for (DTOEntity e : dtoEntities
        ) {
            populationList.add(new PopulationData(e.getName(), e.instanceQuantityProperty()));
        }

        entitiesTV.setItems(populationList);
    }

    /**
     * Clears the entitiesTV from previous info.
     */
    public void clearExecutionDetails() {
        entitiesTV.getItems().clear();
    }
}
