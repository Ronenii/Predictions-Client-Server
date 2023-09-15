package gui.result.details;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.objects.DTOEntityPopulation;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.result.ResultComponentController;
import gui.result.details.control.bar.ExecutionDetailsControlBarController;
import gui.result.models.PopulationData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import simulation.objects.world.status.SimulationStatus;
import ui2engine.simulation.control.bar.DTOSimulationControlBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExecutionDetailsComponentController {
    private ResultComponentController mainController;
    @FXML
    private TableView<PopulationData> entitiesTV;

    @FXML
    private TableColumn<PopulationData, String> entityColumn;

    @FXML
    private TableColumn<PopulationData, Number> quantityColumn;

    private Map<String, PopulationData> populationDataMap;

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

        entityColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationDataMap = new HashMap<>();
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

        currentTickDetLabel.textProperty().bind(ticksProperty);
        durationDetLabel.textProperty().bind(durationProperty);
        simulationIdDetLabel.textProperty().bind(simIdProperty);

        // Binds the enabling and disabling of the control bar to this property
        statusProperty.addListener(((observable, oldValue, newValue) -> {
            switch (SimulationStatus.valueOf(newValue.toString().toUpperCase())) {
                case COMPLETED:
                    controlBarAnchorPane.disableProperty().set(true);
                    break;
                case WAITING:
                case ONGOING:
                    controlBarAnchorPane.disableProperty().set(false);
                    break;
            }
        }));
    }

    /**
     * Updates the components according to the given simulation run data.
     */
    public void updateToChosenSimulation(SimulationRunData runData) {
        //clearExecutionDetails();
        ticksProperty.set(String.valueOf(runData.getTick()));
        durationProperty.set(formatTime(runData.getTime()));
        statusProperty.set(runData.getStatus());
        simIdProperty.set(String.valueOf(runData.getSimId()));
        updateEntitiesTV(runData.getEntityPopulation());
    }

    private String formatTime(long time){
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    /**
     * @param dtoEntities The entities we want to display the quantities of in the
     *                    entitiesTV.
     */
    private void updateEntitiesTV(List<DTOEntityPopulation> dtoEntities) {
        for (DTOEntityPopulation entityPopulation : dtoEntities) {
            if (populationDataMap.containsKey(entityPopulation.getEntityName())) {
                populationDataMap.get(entityPopulation.getEntityName()).populationProperty().set(entityPopulation.getPopulation());
            } else {
                PopulationData populationData = new PopulationData(entityPopulation.getEntityName(), entityPopulation.getPopulation());
                entitiesTV.getItems().add(populationData);
                populationDataMap.put(entityPopulation.getEntityName(), populationData);
            }
        }
    }

    /**
     * Clears the entitiesTV from previous info.
     */
    public void clearExecutionDetails() {
        entitiesTV.getItems().clear();
    }

    public void sendStopToTheEngine() {
        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(true, false, false);
        mainController.getEngineAgent().setStopPauseOrPlayForSimById(simulationIdDetLabel.getText(), dtoSimulationControlBar);
    }

    public void sendPauseToTheEngine() {
        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(false, true, false);
        mainController.getEngineAgent().setStopPauseOrPlayForSimById(simulationIdDetLabel.getText(), dtoSimulationControlBar);
    }

    public void sendPlayToTheEngine() {
        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(false, false, true);
        mainController.getEngineAgent().setStopPauseOrPlayForSimById(simulationIdDetLabel.getText(), dtoSimulationControlBar);
    }
}
