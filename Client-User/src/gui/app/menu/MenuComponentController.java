package gui.app.menu;

import gui.api.Controller;
import gui.app.menu.request.data.RequestData;
import server2client.simulation.prview.SimulationsPreviewData;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.UserAppController;
import gui.app.menu.execution.NewExecutionComponentController;
import gui.app.menu.request.RequestComponentController;
import gui.app.menu.result.ResultComponentController;
import gui.app.menu.simulation.breakdown.SimBreakdownMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class MenuComponentController implements Controller {
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

    public TabPane getMenusTabPane(){
        return menusTabPane;
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData, RequestData requestData){
        resultComponentController.addSimulationToQueue(simulationRunData, requestData);
    }

    public void moveToExecutionSetUp(RequestData requestData) {
        newExecutionComponentController.setUpExecutionWindow(requestData);
    }

    public RequestData getRequestDataById(int reqId){
        return requestComponentController.getRequestDataById(reqId);
    }

    public void refreshRequestsTv(){
        requestComponentController.refreshRequestsTv();
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }

    public void updateNewRequestComponent(SimulationsPreviewData simulationsPreviewData) {
        requestComponentController.updateNewRequestComponent(simulationsPreviewData);
    }
}
