package gui.app.menu;


import gui.app.AdminAppController;
import gui.app.api.Controller;
import gui.app.menu.allocation.AllocationComponentController;
import gui.app.menu.execution.ExecutionComponentController;
import gui.app.menu.management.ManagementComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import server2client.simulation.admin.load.details.AdminLoadDetails;

public class MenuComponentController implements Controller {
    private AdminAppController mainController;

    @FXML
    private GridPane managementComponent;
    @FXML
    private ManagementComponentController managementComponentController;

    @FXML
    private GridPane executionComponent;
    @FXML
    private ExecutionComponentController executionComponentController;

    @FXML
    private GridPane allocationComponent;
    @FXML
    private AllocationComponentController allocationComponentController;
    public void setMainController(AdminAppController controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        if (allocationComponentController != null && managementComponentController != null && executionComponentController != null) {
            allocationComponentController.setMainController(this);
            managementComponentController.setMainController(this);
            executionComponentController.setMainController(this);
        }
    }

    public boolean isThreadPoolSet() {
        return managementComponentController.isThreadPoolSet();
    }

    public void receiveAdminLoadDetails(AdminLoadDetails adminLoadDetails) {
        if(adminLoadDetails.getSimulationsNames().length != 0) {
            managementComponentController.receiveAdminLoadSimulationNames(adminLoadDetails.getSimulationsNames());
        }
        if(adminLoadDetails.isThreadPoolInitialized()) {
            managementComponentController.turnOnThreadDataRefresher();
        }
        if(adminLoadDetails.getUnansweredRequests() != null) {
            allocationComponentController.updateAllocationTableView(adminLoadDetails.getUnansweredRequests());
        }
        if(adminLoadDetails.getLoadedSimulations() != null) {
            executionComponentController.fetchAdminLoadSimulations(adminLoadDetails.getLoadedSimulations());
        }
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }


}
