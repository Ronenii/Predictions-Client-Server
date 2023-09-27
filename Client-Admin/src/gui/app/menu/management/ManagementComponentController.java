package gui.app.menu.management;

import gui.app.menu.MenuComponentController;
import gui.app.menu.management.simulation.SimulationManagerComponentController;
import gui.app.menu.management.thread.ThreadManagerComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class ManagementComponentController {
    private MenuComponentController mainController;

    @FXML
    private GridPane simulationManagerComponent;

    @FXML
    private SimulationManagerComponentController simulationManagerComponentController;

    @FXML
    private GridPane threadManagerComponent;

    @FXML
    private ThreadManagerComponentController threadManagerComponentController;

    public void setMainController(MenuComponentController menuComponentController){
        this.mainController = menuComponentController;
    }

    @FXML
    public void initialize() {
        if(simulationManagerComponentController != null && threadManagerComponentController != null){
            simulationManagerComponentController.setMainController(this);
            threadManagerComponentController.setMainController(this);
        }
    }
}
