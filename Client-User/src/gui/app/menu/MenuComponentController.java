package gui.app.menu;

import gui.api.Controller;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.runtime.SimulationRunData;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.api.HasFileLoadedListeners;
import gui.app.UserAppController;
import gui.app.menu.execution.NewExecutionComponentController;
import gui.app.menu.request.RequestComponentController;
import gui.app.menu.result.ResultComponentController;
import gui.app.menu.simulation.breakdown.SimBreakdownMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import manager.UserServerAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class MenuComponentController implements HasFileLoadedListeners, UserEngineCommunicator, Controller {
    private UserAppController mainController;
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
    @FXML private GridPane requestComponent;
    @FXML private RequestComponentController requestComponentController;

    public void setMainController(UserAppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(simBreakdownMenuController != null && newExecutionComponentController != null && resultComponentController != null && requestComponentController != null) {
            simBreakdownMenuController.setMainController(this);
            newExecutionComponentController.setMainController(this);
            resultComponentController.setMainController(this);
            requestComponentController.setMainController(this);
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
    public UserServerAgent getEngineAgent() {
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

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
