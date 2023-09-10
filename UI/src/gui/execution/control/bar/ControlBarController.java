package gui.execution.control.bar;

import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.execution.NewExecutionComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import manager.EngineAgent;

public class ControlBarController implements BarNotifier, EngineCommunicator {
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
        getEngineAgent().
    }

    @FXML
    void startButtonActionListener(ActionEvent event) {

    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @Override
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }
}