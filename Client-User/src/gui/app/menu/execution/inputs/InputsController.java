package gui.app.menu.execution.inputs;
import gui.app.menu.execution.inputs.entity.EntityPopulationComponentController;
import gui.app.menu.execution.inputs.env.var.EnvironmentVariableComponentController;
import gui.app.menu.execution.models.EntitiesStartData;
import gui.app.menu.execution.models.EnvironmentVarsStartData;
import gui.app.menu.execution.models.StartDetails;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.api.HasFileLoadedListeners;
import gui.app.menu.execution.NewExecutionComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import manager.UserServerAgent;
import client2server.simulation.execution.DTOExecutionData;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;

public class InputsController implements HasFileLoadedListeners, BarNotifier, UserEngineCommunicator {
    private NewExecutionComponentController mainController;
    @FXML private GridPane entityPopulationComponent;
    @FXML private EntityPopulationComponentController entityPopulationComponentController;
    @FXML private GridPane environmentVariablesComponent;
    @FXML private EnvironmentVariableComponentController environmentVariableComponentController;

    public DTOExecutionData executionData;

    public void setMainController(NewExecutionComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize(){
        if(environmentVariableComponentController != null && entityPopulationComponentController != null){
            entityPopulationComponentController.setMainController(this);
            environmentVariableComponentController.setMainController(this);
        }
    }

    @Override
    public List<EventListener> getAllFileLoadedListeners() {
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(entityPopulationComponentController);
        listeners.add(environmentVariableComponentController);

        return listeners;
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @Override
    public UserServerAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public void clearInputs(){
        entityPopulationComponentController.clearInputs();
        environmentVariableComponentController.clearInputs();
    }

    public StartDetails getStartDetails(Map<String, Object> envVarsValuesMap) {
        EntitiesStartData entitiesStartData = entityPopulationComponentController.getEntitiesStartData();
        EnvironmentVarsStartData environmentVarsStartData = new EnvironmentVarsStartData(envVarsValuesMap);
        return new StartDetails(entitiesStartData, environmentVarsStartData);
    }

    public void fetchStartDetails(StartDetails startDetails) {
        entityPopulationComponentController.fetchEntitiesStartData(startDetails.getEntitiesStartData());
        environmentVariableComponentController.fetchEnvironmentVarsStartData(startDetails.getEnvironmentVarsStartData());
    }
}
