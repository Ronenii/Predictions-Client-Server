package gui.app.menu;


import gui.app.AdminAppController;
import gui.app.api.Controller;
import gui.app.menu.allocation.AllocationComponentController;
import gui.app.menu.management.ManagementComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class MenuComponentController implements Controller {
    private Controller mainController;

    @FXML
    private GridPane managementComponent;
    @FXML
    private ManagementComponentController managementComponentController;


    @FXML
    private GridPane allocationComponent;
    @FXML
    private AllocationComponentController allocationComponentController;

    @Override
    public void setMainController(Controller controller) {
        this.mainController = controller;
    }

//    public void setMainController(AdminAppController adminAppController) {
//        mainController = adminAppController;
//    }

    @FXML
    public void initialize() {
        if (allocationComponentController != null && managementComponentController != null) {
            allocationComponentController.setMainController(this);
            managementComponentController.setMainController(this);
        }
    }


    @Override
    public void showMessageInNotificationBar(String message) {

    }

    @Override
    public void showAlertAndWait(String message) {

    }
}
