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

    public void setMainController(MenuComponentController controller){
        this.mainController = controller;
    }
    @FXML
    public void initialize() {
        if (simulationManagerComponentController != null && threadManagerComponentController != null) {
            simulationManagerComponentController.setMainController(this);
            threadManagerComponentController.setMainController(this);
        }
    }

    public String getSelectedSimulationName() {
        return simulationManagerComponentController.getSelectedSimulationName();
    }

    public void enableThreadComponent() {
        threadManagerComponentController.enableThreadComponent();
    }

    public boolean isThreadPoolSet() {
        return threadManagerComponentController.isThreadPoolSet();
    }

    public void receiveAdminLoadSimulationNames(String[] simulationNames) {
        simulationManagerComponentController.addAdminLoadSimulationNames(simulationNames);
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
