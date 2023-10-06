package gui.app.menu.execution;

import server2client.simulation.runtime.SimulationRunData;
import gui.app.menu.execution.control.bar.ControlBarController;
import gui.app.menu.execution.inputs.InputsController;
import gui.app.menu.execution.models.StartDetails;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.api.HasFileLoadedListeners;
import gui.app.menu.MenuComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import manager.UserEngineAgent;

import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewExecutionComponentController implements HasFileLoadedListeners, BarNotifier, UserEngineCommunicator {
    private MenuComponentController mainController;
    @FXML private GridPane inputs;
    @FXML private InputsController inputsController;
    @FXML private GridPane controlBar;
    @FXML private ControlBarController controlBarController;

    private Map<String, StartDetails> startDetailsMap;

    public void setMainController(MenuComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(inputsController != null && controlBarController != null) {
            inputsController.setMainController(this);
            controlBarController.setMainController(this);
        }

        startDetailsMap = new HashMap<>();
    }

    @Override
    public List<EventListener> getAllFileLoadedListeners() {
        return inputsController.getAllFileLoadedListeners();
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @Override
    public UserEngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public void clearInputs(){
        inputsController.clearInputs();
    }

    /**
     * @return The tab pane component of the menus
     */
    public TabPane getMenusTabPane(){
        return mainController.getMenusTabPane();
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData){
        mainController.addSimulationToQueue(simulationRunData);
    }

    public void updateStartDetailsMap(String simId, Map<String, Object> envVarsValuesMap) {
        StartDetails startDetails = inputsController.getStartDetails(envVarsValuesMap);
        startDetailsMap.put(simId, startDetails);
    }

    public void rerunSimulationById(String simId) {
        StartDetails rerunStartDetails = startDetailsMap.get(simId);
        inputsController.fetchStartDetails(rerunStartDetails);
    }
}
