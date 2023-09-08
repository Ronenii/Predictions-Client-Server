package gui.execution.inputs;

import gui.api.event.ErrorEvent;
import gui.api.event.handler.BarNotifier;
import gui.api.event.handler.HasFileLoadedListeners;
import gui.execution.NewExecutionComponentController;
import gui.execution.inputs.entity.EntityPopulationComponentController;
import gui.execution.inputs.env.var.EnvironmentVariablesComponentController;
import gui.notification.NotificationBarComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import ui2engine.simulation.func3.DTOExecutionData;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class InputsController implements HasFileLoadedListeners, BarNotifier {
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
    public BarNotifier getNotificationBarParent() {
        return mainController.getNotificationBarParent();
    }
}
