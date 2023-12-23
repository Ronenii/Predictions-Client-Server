package gui.app.menu.execution.details;

import gui.app.menu.execution.ExecutionComponentController;
import server2client.simulation.genral.impl.objects.DTOEntityPopulation;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.api.Controller;
import gui.app.menu.execution.details.data.PopulationData;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExecutionDetailsComponentController implements Controller {
    private Controller mainController;
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
    private Label simulationNameLabel;

    private SimpleStringProperty ticksProperty;
    private SimpleStringProperty durationProperty;
    private SimpleStringProperty simIdProperty;
    private SimpleStringProperty statusProperty;
    private SimpleStringProperty simulationNameProperty;

    private String currentSimId = null;


    public void setMainController(ExecutionComponentController controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
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
        simulationNameProperty = new SimpleStringProperty();

        currentTickDetLabel.textProperty().bind(ticksProperty);
        durationDetLabel.textProperty().bind(durationProperty);
        simulationIdDetLabel.textProperty().bind(simIdProperty);
        simulationNameLabel.textProperty().bind(simulationNameProperty);


        // Binds the enabling and disabling of the control bar to this property
        statusProperty.addListener(((observable, oldValue, newValue) -> {
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
        simulationNameProperty.set(runData.getName());
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

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }

//    public void sendStopToTheEngine() {
//        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(true, false, false, false);
//        mainController.getEngineAgent().setStopPausePlayOrSkipFwdForSimById(simulationIdDetLabel.getText(), dtoSimulationControlBar);
//    }
//
//    public void sendPauseToTheEngine() {
//        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(false, true, false, false);
//        mainController.getEngineAgent().setStopPausePlayOrSkipFwdForSimById(simulationIdDetLabel.getText(), dtoSimulationControlBar);
//    }
//
//    public void sendPlayToTheEngine() {
//        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(false, false, true, false);
//        mainController.getEngineAgent().setStopPausePlayOrSkipFwdForSimById(simulationIdDetLabel.getText(), dtoSimulationControlBar);
//    }
//
//    public void sendSkipForwardToTheEngine() {
//        DTOSimulationControlBar dtoSimulationControlBar = new DTOSimulationControlBar(false, true, false, true);
//        isPlayButtonClicked = false;
//        skipOne = true;
//        skipForwardTask(dtoSimulationControlBar);
//    }
//
//    private void skipForwardTask(DTOSimulationControlBar dtoSimulationControlBar) {
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                do {
//                    if(skipOne){
//                        mainController.getEngineAgent().setStopPausePlayOrSkipFwdForSimById(simulationIdDetLabel.getText(), dtoSimulationControlBar);
//                        setExecutionQueueTaskOnSkipForward();
//                        skipOne = false;
//                    }
//
//                    Thread.sleep(200); // Make the thread sleep for 200ms
//                } while (!isPlayButtonClicked);
//                return null;
//            }
//        };
//
//        runTask(task);
//    }
//
//    /**
//     * Given a task, this creates a thread for it and runs it.
//     */
//    private void runTask(Task<Void> task) {
//        Thread thread = new Thread(task);
//        thread.setDaemon(true); // Mark the thread as a daemon to allow application exit
//        thread.start();
//    }
//
//    public void setPlayButtonClicked(boolean playButtonClicked) {
//        isPlayButtonClicked = playButtonClicked;
//    }
//
//    public void setSkipOne(boolean skipOne) {
//        this.skipOne = skipOne;
//    }
//
//    public void setExecutionQueueTaskOnSkipForward() {
//        mainController.setExecutionQueueTaskOnSkipForward();
//    }
//
//    public void setExecutionQueueTaskOnPause() {
//        mainController.setExecutionQueueTaskOnPause();
//    }
//
//    public void disableExecutionQueueTaskOnPause() {
//        mainController.disableExecutionQueueTaskOnPause();
//    }
//
//    public int getSimulationCurrentTicks() {
//        return Integer.parseInt(currentTickDetLabel.getText());
//    }
//
//    @FXML
//    void rerunButtonActionListener(ActionEvent event) {
//        mainController.rerunSimulationById(simulationIdDetLabel.getText());
//        mainController.getMenusTabPane().getSelectionModel().selectPrevious();
//    }
//
//    public void setOneUpdateAfterPauseFlag() {
//        mainController.setOneUpdateAfterPauseFlag();
//    }
}
