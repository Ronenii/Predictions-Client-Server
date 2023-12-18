package gui.app.menu.result.details;

import gui.api.Controller;
import gui.app.menu.result.details.refresher.SkipForwardRefresher;
import manager.UserServerAgent;
import server2client.simulation.genral.impl.objects.DTOEntityPopulation;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.menu.result.details.control.bar.ExecutionDetailsControlBarController;
import gui.app.menu.result.ResultComponentController;
import gui.app.menu.result.models.PopulationData;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import simulation.objects.world.status.SimulationStatus;
import client2server.simulation.control.bar.DTOSimulationControlBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExecutionDetailsComponentController implements Controller {
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
    private String currentSimId = null;
    private boolean isPlayButtonClicked;
    private boolean skipOne;

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
        isPlayButtonClicked = true;
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
        if(currentSimId == null || !currentSimId.equals(runData.getSimId())){
            currentSimId = runData.getSimId();
            entitiesTV.getItems().clear();
        }

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
    private void updateEntitiesTV(DTOEntityPopulation[] dtoEntities) {
        for (DTOEntityPopulation entityPopulation : dtoEntities) {
            if (populationDataMap.containsKey(entityPopulation.getEntityName())) {
                // if the client switched to other simulation, the entities table view will be empty.
                if(!isEntityInTv(entityPopulation.getEntityName())){
                    PopulationData populationData = populationDataMap.get(entityPopulation.getEntityName());
                    entitiesTV.getItems().add(populationData);
                }

                populationDataMap.get(entityPopulation.getEntityName()).populationProperty().set(entityPopulation.getPopulation());
            } else {
                PopulationData populationData = new PopulationData(entityPopulation.getEntityName(), entityPopulation.getPopulation());
                entitiesTV.getItems().add(populationData);
                populationDataMap.put(entityPopulation.getEntityName(), populationData);
            }
        }
    }

    private boolean isEntityInTv(String entityName){
        for(PopulationData populationData : entitiesTV.getItems()) {
            if (populationData.getName().equals(entityName)){
                return true;
            }
        }

        return false;
    }

    public void sendStopToTheEngine() {
        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(true, false, false, false);
        UserServerAgent.setStopPausePlayOrSkipFwdForSimById(this, simulationIdDetLabel.getText(), dtoSimulationControlBar);
    }

    public void sendPauseToTheEngine() {
        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(false, true, false, false);
        UserServerAgent.setStopPausePlayOrSkipFwdForSimById(this, simulationIdDetLabel.getText(), dtoSimulationControlBar);
    }

    public void sendPlayToTheEngine() {
        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(false, false, true, false);
        UserServerAgent.setStopPausePlayOrSkipFwdForSimById(this, simulationIdDetLabel.getText(), dtoSimulationControlBar);
    }

    public void sendSkipForwardToTheEngine() {
        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(false, true, false, true);
        isPlayButtonClicked = false;
        skipOne = true;
        skipForwardTask(dtoSimulationControlBar, this);
    }


    private void skipForwardTask(DTOSimulationControlBar dtoSimulationControlBar, Controller controller) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                do {
                    if(skipOne){
                        UserServerAgent.setStopPausePlayOrSkipFwdForSimById(controller, simulationIdDetLabel.getText(), dtoSimulationControlBar);
                        setExecutionQueueTaskOnSkipForward();
                        skipOne = false;
                    }

                    Thread.sleep(200); // Make the thread sleep for 200ms
                } while (!isPlayButtonClicked);
                return null;
            }
        };

        runTask(task);
    }

    /**
     * Given a task, this creates a thread for it and runs it.
     */
    private void runTask(Task<Void> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true); // Mark the thread as a daemon to allow application exit
        thread.start();
    }

    public void setPlayButtonClicked(boolean playButtonClicked) {
        isPlayButtonClicked = playButtonClicked;
    }

    public void setSkipOne(boolean skipOne) {
        this.skipOne = skipOne;
    }

    public void setExecutionQueueTaskOnSkipForward() {
        mainController.setExecutionQueueTaskOnSkipForward();
    }

    public void setExecutionQueueTaskOnPause() {
        mainController.setExecutionQueueTaskOnPause();
    }

    public void disableExecutionQueueTaskOnPause() {
        mainController.disableExecutionQueueTaskOnPause();
    }

    public int getSimulationCurrentTicks() {
        return Integer.parseInt(currentTickDetLabel.getText());
    }

    public void setOneUpdateAfterPauseFlag() {
        mainController.setOneUpdateAfterPauseFlag();
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
