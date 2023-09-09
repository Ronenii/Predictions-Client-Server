package gui.execution;

import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.api.HasFileLoadedListeners;
import gui.execution.control.bar.ControlBarController;
import gui.execution.inputs.InputsController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;
import ui2engine.simulation.execution.DTOExecutionData;

import java.util.EventListener;
import java.util.List;

public class NewExecutionComponentController implements HasFileLoadedListeners, BarNotifier, EngineCommunicator {
    private SubMenusController mainController;
    @FXML private GridPane inputs;
    @FXML private InputsController inputsController;
    @FXML private GridPane controlBar;
    @FXML private ControlBarController controlBarController;

    public EngineAgent engineAgent;

    public void setMainController(SubMenusController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(inputsController != null && controlBarController != null) {
            inputsController.setMainController(this);
            controlBarController.setMainController(this);
        }
    }

    @Override
    public List<EventListener> getAllFileLoadedListeners() {
        return inputsController.getAllFileLoadedListeners();
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    public DTOExecutionData getExecutionData(){
        return inputsController.executionData;
    }

    @Override
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }
}
