package gui.app.menu;


import gui.app.AdminAppController;
import gui.app.menu.allocation.AllocationComponentController;
import gui.app.menu.managment.ManagementComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class MenuComponentController {
    private AdminAppController mainController;

    @FXML
    private GridPane managementComponent;
    @FXML
    private ManagementComponentController managementComponentController;


    @FXML
    private GridPane allocationComponent;
    @FXML
    private AllocationComponentController allocationComponentController;

    public void setMainController(AdminAppController adminAppController) {
        mainController = adminAppController;
    }

    @FXML
    public void initialize() {
        if (allocationComponentController != null && managementComponentController != null) {
            allocationComponentController.setMainController(this);
            managementComponentController.setMainController(this);
        }
    }


}
