package fixed.app.menu;

import engine2ui.simulation.prview.PreviewData;
import engine2ui.simulation.runtime.SimulationRunData;
import gui.api.BarNotifier;
import fixed.api.UserEngineCommunicator;
import gui.api.HasFileLoadedListeners;
import fixed.app.AppController;
import fixed.app.menu.execution.NewExecutionComponentController;
import fixed.app.menu.result.ResultComponentController;
import fixed.app.menu.simulation.breakdown.SimBreakdownMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import manager.UserEngineAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class SubMenusController implements HasFileLoadedListeners, BarNotifier, UserEngineCommunicator {
    private AppController mainController;
    @FXML private GridPane currentMainComponent;
    @FXML private TabPane menusTabPane;
    @FXML private Tab detailsTab;
    @FXML private Tab newExecTab;
    @FXML private Tab resultTab;

    @FXML private GridPane simBreakdownMenu;
    @FXML private SimBreakdownMenuController simBreakdownMenuController;
    @FXML private GridPane newExecutionComponent;
    @FXML private NewExecutionComponentController newExecutionComponentController;
    @FXML private GridPane resultComponent;
    @FXML private ResultComponentController resultComponentController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(simBreakdownMenuController != null && newExecutionComponentController != null && resultComponentController != null) {
            simBreakdownMenuController.setMainController(this);
            newExecutionComponentController.setMainController(this);
            resultComponentController.setMainController(this);
        }
    }

    public void setPreviewDataForSimBreakdown(PreviewData previewData){
        simBreakdownMenuController.setPreviewData(previewData);
    }

    @Override
    public List<EventListener> getAllFileLoadedListeners() {
        List<EventListener> listeners = new ArrayList<>();
        listeners.addAll(simBreakdownMenuController.getAllFileLoadedListeners());
        listeners.addAll(newExecutionComponentController.getAllFileLoadedListeners());
        listeners.add(resultComponentController);
        return listeners;
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }
    @Override
    public UserEngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public TabPane getMenusTabPane(){
        return menusTabPane;
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData){
        resultComponentController.addSimulationToQueue(simulationRunData);
    }

    public void rerunSimulationById(String simId) {
        newExecutionComponentController.rerunSimulationById(simId);
    }


}
