package gui.app.menu.request.table;

import client2server.simulation.request.DTORequest;
import gui.api.Controller;
import gui.app.menu.request.RequestComponentController;
import gui.app.menu.request.data.RequestData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.HashMap;
import java.util.Map;

public class RequestTableComponentController implements Controller {
    private RequestComponentController mainController;

    private Map<Integer, RequestData> requestDataMap = new HashMap<>();

    @FXML
    private Button executeBTN;

    @FXML
    private TableView<?> requestsTV;

    @FXML
    private TableColumn<String, String> requestIdColumn;

    @FXML
    private TableColumn<String, String> simulationNameColumn;

    @FXML
    private TableColumn<String, String> tokensColumn;

    @FXML
    private TableColumn<String, String> statusColumn;

    @FXML
    private TableColumn<String, String> runningColumn;

    @FXML
    private TableColumn<String, String> finishedColumn;

    @FXML
    void executeButtonActionListener(ActionEvent event) {

    }

    public void addNewRequestData(int requestId, DTORequest dtoRequest) {
        RequestData newRequestData = new RequestData(requestId, dtoRequest);
        requestDataMap.put(requestId, newRequestData);
    }

    public void setMainController(RequestComponentController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
