package gui.app.menu.management;

import gui.app.api.Controller;
import gui.app.menu.MenuComponentController;
import gui.app.menu.management.simulation.SimulationManagerComponentController;
import gui.app.menu.management.thread.ThreadManagerComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class ManagementComponentController implements Controller {
    private Controller mainController;

    @FXML
    private GridPane simulationManagerComponent;

    @FXML
    private SimulationManagerComponentController simulationManagerComponentController;

    @FXML
    private GridPane threadManagerComponent;

    @FXML
    private ThreadManagerComponentController threadManagerComponentController;

//    public void setMainController(MenuComponentController menuComponentController) {
//        this.mainController = menuComponentController;
//    }

    @Override
    public void setMainController(Controller controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        if (simulationManagerComponentController != null && threadManagerComponentController != null) {
            simulationManagerComponentController.setMainController(this);
            threadManagerComponentController.setMainController(this);
        }
    }

    @Override
    public void showMessageInNotificationBar(String message) {

    }

    @Override
    public void showAlertAndWait(String message) {

    }
}
