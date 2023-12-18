package gui.app.menu.execution;

import gui.api.Controller;
import gui.app.menu.request.data.RequestData;
import server2client.simulation.runtime.SimulationRunData;
import gui.app.menu.execution.control.bar.ControlBarController;
import gui.app.menu.execution.inputs.InputsController;
import gui.app.menu.MenuComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import manager.UserServerAgent;


public class NewExecutionComponentController implements Controller{
    private MenuComponentController mainController;
    @FXML private GridPane inputs;
    @FXML private InputsController inputsController;
    @FXML private GridPane controlBar;
    @FXML private ControlBarController controlBarController;

    public void setMainController(MenuComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(inputsController != null && controlBarController != null) {
            inputsController.setMainController(this);
            controlBarController.setMainController(this);
        }
    }

    public void clearInputs(){
        inputsController.clearInputs();
    }

    /**
     * @return The tab pane component of the menus
     */
    public TabPane getMenusTabPane(){
        return mainController.getMenusTabPane();
    }

    public void addSimulationToQueue(SimulationRunData simulationRunData){
        mainController.addSimulationToQueue(simulationRunData, mainController.getRequestDataById(getCurrentReqId()));
    }

    public void setUpExecutionWindow(RequestData requestData) {
        // Receiving from the server the preview data in order to update the execution window according to the chosen request's simulation.
        UserServerAgent.getSimulationPreviewDataForExecutionWindow(inputsController, requestData);
        controlBarController.setButtonsDisableOff();
    }


    public int getCurrentReqId() {
        return inputsController.getCurrentReqId();
    }

    /**
     * If the given request has tokens left to run the simulation - return true.
     */
    public boolean requestHasTokens(int reqId){
        RequestData requestData = mainController.getRequestDataById(reqId);

        return requestData.getTokens() != 0;
    }

    public void decreaseGivenRequestsTokensCount(int reqId) {
        mainController.getRequestDataById(reqId).decreaseTokens();
        mainController.refreshRequestsTv();
    }


    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
