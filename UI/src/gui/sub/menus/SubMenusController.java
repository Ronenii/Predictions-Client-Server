package gui.sub.menus;

import engine2ui.simulation.prview.PreviewData;
import gui.api.event.ErrorEvent;
import gui.api.event.handler.BarNotifier;
import gui.api.event.handler.HasFileLoadedListeners;
import gui.app.AppController;
import gui.execution.NewExecutionComponentController;
import gui.notification.NotificationBarComponentController;
import gui.result.ResultComponentController;
import gui.simulation.breakdown.SimBreakdownMenuController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class SubMenusController implements HasFileLoadedListeners, BarNotifier {
    private AppController mainController;
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
        return listeners;
    }

    @Override
    public BarNotifier getNotificationBarParent() {
        return mainController.getNotificationBarParent();
    }
}
