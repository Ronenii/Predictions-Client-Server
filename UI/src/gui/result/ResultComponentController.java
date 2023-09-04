package gui.result;

import gui.result.execution.details.ExecutionDetailsComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class ResultComponentController {
    private SubMenusController mainController;
    @FXML
    private Label exeListLabel;
    @FXML
    private ListView<?> executionsLV;
    @FXML
    private GridPane executionDetailsComponent;
    @FXML
    private ExecutionDetailsComponentController executionDetailsComponentController;

    public void setMainController(SubMenusController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        if(executionDetailsComponentController != null) {
            executionDetailsComponentController.setMainController(this);
        }
    }
}









