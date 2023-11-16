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
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestTableComponentController implements Controller {
    private RequestComponentController mainController;

    private Map<Integer, RequestData> requestDataMap = new HashMap<>();

    @FXML
    private Button executeBTN;

    @FXML
    private TableView<RequestData> requestsTV;

    @FXML
    private TableColumn<RequestData, Number> requestIdColumn;

    @FXML
    private TableColumn<RequestData, String> simulationNameColumn;

    @FXML
    private TableColumn<RequestData, Number> tokensColumn;

    @FXML
    private TableColumn<RequestData, String> statusColumn;

    @FXML
    private TableColumn<RequestData, Number> runningColumn;

    @FXML
    private TableColumn<RequestData, Number> finishedColumn;

    @FXML
    public void initialize() {
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        simulationNameColumn.setCellValueFactory(new PropertyValueFactory<>("simulationName"));
        tokensColumn.setCellValueFactory(new PropertyValueFactory<>("tokens"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        runningColumn.setCellValueFactory(new PropertyValueFactory<>("running"));
        finishedColumn.setCellValueFactory(new PropertyValueFactory<>("finished"));
    }

    @FXML
    void executeButtonActionListener(ActionEvent event) {

    }

    public void addNewRequestData(int requestId, DTORequest dtoRequest) {
        RequestData newRequestData = new RequestData(requestId, dtoRequest);
        requestDataMap.put(requestId, newRequestData);
        requestsTV.getItems().add(newRequestData);
    }

    public void updateRequestsTableView(DTORequest dtoRequest) {

    }

    public void setMainController(RequestComponentController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
