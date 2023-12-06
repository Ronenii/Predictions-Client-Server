package gui.app.menu.request;

import client2server.simulation.request.DTORequest;
import gui.api.Controller;
import gui.app.menu.MenuComponentController;
import gui.app.menu.request.create.request.NewRequestComponentController;
import gui.app.menu.request.data.RequestData;
import gui.app.menu.request.table.RequestTableComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import server2client.simulation.prview.SimulationsPreviewData;

public class RequestComponentController implements Controller {
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

    public void addNewRequestData(int requestId, DTORequest dtoRequest) {
        requestTableComponentController.addNewRequestData(requestId, dtoRequest);
    }


    public void moveToExecutionSetUp(RequestData requestData) {
        mainController.moveToExecutionSetUp(requestData);
        mainController.getMenusTabPane().getSelectionModel().selectNext();
    }

    public RequestData getRequestDataById(int reqId){
       return requestTableComponentController.getRequestDataById(reqId);
    }

    public void refreshRequestsTv(){
        requestTableComponentController.refreshRequestsTv();
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
