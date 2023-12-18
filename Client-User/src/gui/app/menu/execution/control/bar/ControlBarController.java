package gui.app.menu.execution.control.bar;

import gui.api.Controller;
import server2client.simulation.execution.StartResponse;
import gui.app.menu.execution.NewExecutionComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import manager.UserServerAgent;

public class ControlBarController implements Controller {
    private NewExecutionComponentController mainController;
    @FXML
    private Button startBTN;

    @FXML
    private Button clearBTN;

    public void setMainController(NewExecutionComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void clearButtonActionListener(ActionEvent event) {
        mainController.clearInputs();
        showMessageInNotificationBar("Cleared all inputs from user.");
    }

    @FXML
    void startButtonActionListener(ActionEvent event) {
        if(mainController.requestHasTokens(mainController.getCurrentReqId())) {
            UserServerAgent.startSimulation(this, mainController.getCurrentReqId());
        } else {
            showMessageInNotificationBar("No more tokens left!");
        }
    }

    public void receiveStartResponse(StartResponse response){
        if(response.isSuccess()){
            mainController.getMenusTabPane().getSelectionModel().selectLast();
            mainController.addSimulationToQueue(response.getSimulationRunData());
            mainController.decreaseGivenRequestsTokensCount(mainController.getCurrentReqId());
        }

        showMessageInNotificationBar(response.getMessage());
    }

    public void setButtonsDisableOff() {
        startBTN.setDisable(false);
        clearBTN.setDisable(false);
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}