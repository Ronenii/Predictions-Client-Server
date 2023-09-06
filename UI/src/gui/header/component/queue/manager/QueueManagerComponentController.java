package gui.header.component.queue.manager;

import gui.header.component.HeaderComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QueueManagerComponentController {

    private HeaderComponentController mainController;
    @FXML
    private Label lblQueue;

    @FXML
    private Label lblRunning;

    @FXML
    private Label lblEnded;

    public void setMainController(HeaderComponentController mainController) {
        this.mainController = mainController;
    }

}

