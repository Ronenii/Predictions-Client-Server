package gui.execution.inputs;

import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.api.HasFileLoadedListeners;
import gui.execution.NewExecutionComponentController;
import gui.execution.inputs.entity.EntityPopulationComponentController;
import gui.execution.inputs.env.var.EnvironmentVariablesComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;
import ui2engine.simulation.execution.DTOExecutionData;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class InputsController implements HasFileLoadedListeners, BarNotifier, EngineCommunicator {
    private NewExecutionComponentController mainController;
    @FXML private GridPane entityPopulationComponent;
    @FXML private EntityPopulationComponentController entityPopulationComponentController;
    @FXML private GridPane environmentVariablesComponent;
    @FXML private EnvironmentVariablesComponentController environmentVariablesComponentController;

    public DTOExecutionData executionData;

    public void setMainController(NewExecutionComponentController mainController) {
        this.mainController = mainController;
    }

    public void initialize(){
        executionData = new DTOExecutionData();
    }

    @Override
    public List<EventListener> getAllFileLoadedListeners() {
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(entityPopulationComponentController);
        listeners.add(environmentVariablesComponentController);

        return listeners;
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @Override
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }
}
