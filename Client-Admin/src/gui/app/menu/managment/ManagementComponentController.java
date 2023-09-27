package gui.app.menu.managment;

import gui.app.menu.MenuComponentController;
import gui.app.menu.managment.simulation.SimulationManagerComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class ManagementComponentController {
    private MenuComponentController mainController;

    @FXML
    private GridPane simulationManagerComponent;

    @FXML
    private SimulationManagerComponentController simulationManagerComponentController;

    public void setMainController(MenuComponentController menuComponentController){
        this.mainController = menuComponentController;
    }

    @FXML
    public void initialize() {
        if(simulationManagerComponentController != null){
            simulationManagerComponentController.setMainController(this);
        }
    }
}
