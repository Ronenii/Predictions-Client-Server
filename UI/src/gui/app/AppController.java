package gui.app;

import gui.error.ErrorBarComponentController;
import gui.header.component.HeaderComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;

public class AppController {
    @FXML private GridPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;
    @FXML private GridPane subMenus;
    @FXML private SubMenusController subMenusController;

    @FXML private GridPane errorBarComponent;
    @FXML private ErrorBarComponentController errorBarComponentController;

    @FXML private AnchorPane anchorDisplay;

    public EngineAgent engineAgent;

    @FXML
    public void initialize() {
        if(headerComponentController != null && subMenusController != null && errorBarComponentController != null) {
            headerComponentController.setMainController(this);
            subMenusController.setMainController(this);
            errorBarComponentController.setMainController(this);
            engineAgent = new EngineAgent();
        }
    }

    public void showErrorMessage(String errorMessage){
        anchorDisplay.visibleProperty().set(true);
        errorBarComponentController.setLblErrorText(errorMessage);
    }
}
