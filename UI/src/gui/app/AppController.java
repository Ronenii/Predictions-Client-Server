package gui.app;

import gui.header.component.HeaderComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class AppController {
    @FXML private GridPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;
    @FXML private GridPane subMenus;
    @FXML private SubMenusController subMenusController;

    @FXML
    public void initialize() {
        if(headerComponentController != null && subMenusController != null) {
            headerComponentController.setMainController(this);
            subMenusController.setMainController(this);
        }
    }


}
