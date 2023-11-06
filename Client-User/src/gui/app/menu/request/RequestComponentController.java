package gui.app.menu.request;

import gui.app.menu.MenuComponentController;
import gui.app.menu.request.create.request.NewRequestComponentController;
import gui.app.menu.request.table.RequestTableComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import server2client.simulation.prview.SimulationsPreviewData;

public class RequestComponentController {
    private MenuComponentController mainController;

    @FXML private GridPane newRequestComponent;
    @FXML private NewRequestComponentController newRequestComponentController;
    @FXML private GridPane requestTableComponent;
    @FXML private RequestTableComponentController requestTableComponentController;

    @FXML
    public void initialize() {
        if (newRequestComponentController != null && requestTableComponentController != null) {
            newRequestComponentController.setMainController(this);
            requestTableComponentController.setMainController(this);
        }
    }

    public void setMainController(MenuComponentController mainController) {
        this.mainController = mainController;
    }

    public void updateNewRequestComponent(SimulationsPreviewData simulationsPreviewData) {
        newRequestComponentController.updateNewRequestComponent(simulationsPreviewData);
    }
}
