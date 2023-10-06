package gui.sub.menus;

import server2client.simulation.prview.PreviewData;
import server2client.simulation.runtime.SimulationRunData;
import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.api.HasFileLoadedListeners;
import gui.app.AppController;
import gui.app.mode.AppMode;
import gui.execution.NewExecutionComponentController;
import gui.result.ResultComponentController;
import gui.result.models.QueueManagementData;
import gui.simulation.breakdown.SimBreakdownMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class SubMenusController implements HasFileLoadedListeners, BarNotifier, EngineCommunicator {
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
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public TabPane getMenusTabPane(){
        return menusTabPane;
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData){
        resultComponentController.addSimulationToQueue(simulationRunData);
    }

    public void updateQueueLblInQueueManagement() {
        mainController.updateQueueLblInQueueManagement();
    }

    public void updateRunningAndCompletedLblsInQueueManagement(QueueManagementData queueManagementData) {
        mainController.updateRunningAndCompletedLblsInQueueManagement(queueManagementData);
    }

    public void rerunSimulationById(String simId) {
        newExecutionComponentController.rerunSimulationById(simId);
    }

    public void changeToDarkMode() {
        simBreakdownMenuController.changeToDarkMode();
        currentMainComponent.getStylesheets().add(getClass().getResource("themes/DarkMode.css").toExternalForm());
    }

    public void changeToLightMode() {
        simBreakdownMenuController.changeToLightMode();
        currentMainComponent.getStylesheets().add(getClass().getResource("themes/LightMode.css").toExternalForm());
    }

    public boolean isBonusSelected(){
        return mainController.isBonusSelected();
    }

    public void clearMode(AppMode appMode) {
        simBreakdownMenuController.clearMode(appMode);

        switch (appMode) {
            case DARK:
                currentMainComponent.getStylesheets().remove(getClass().getResource("themes/DarkMode.css").toExternalForm());
                break;
            case LIGHT:
                currentMainComponent.getStylesheets().remove(getClass().getResource("themes/LightMode.css").toExternalForm());
                break;
        }
    }
}
