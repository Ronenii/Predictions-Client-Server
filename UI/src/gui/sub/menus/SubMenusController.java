package gui.sub.menus;

import gui.app.AppController;
import gui.execution.NewExecutionComponentController;
import gui.result.ResultComponentController;
import gui.simulation.breakdown.SimBreakdownMenuController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class SubMenusController {
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

}
