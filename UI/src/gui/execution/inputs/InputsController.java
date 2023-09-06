package gui.execution.inputs;

import gui.api.HasFileLoadedListeners;
import gui.execution.NewExecutionComponentController;
import gui.execution.inputs.entity.EntityPopulationComponentController;
import gui.execution.inputs.env.var.EnvironmentVariablesComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class InputsController implements HasFileLoadedListeners {
    private NewExecutionComponentController mainController;
    @FXML private GridPane entityPopulationComponent;
    @FXML private EntityPopulationComponentController entityPopulationComponentController;
    @FXML private GridPane environmentVariablesComponent;
    @FXML private EnvironmentVariablesComponentController environmentVariablesComponentController;

    public void setMainController(NewExecutionComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(entityPopulationComponentController != null && environmentVariablesComponentController != null) {
            entityPopulationComponentController.setMainController(this);
            environmentVariablesComponentController.setMainController(this);
        }
    }

    @Override
    public List<EventListener> getAllFileLoadedListeners() {
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(entityPopulationComponentController);
        listeners.add(environmentVariablesComponentController);

        return listeners;
    }
}
