package gui.app.menu.allocation;

import gui.app.api.Controller;
import gui.app.menu.MenuComponentController;
import gui.app.menu.allocation.data.RequestData;
import gui.app.menu.allocation.refresher.AllocationRefresher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import manager.AdminServerAgent;
import manager.constant.Constants;
import server2client.simulation.request.DTORequests;
import server2client.simulation.request.DTOSingleRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class AllocationComponentController implements Controller {

    private Controller mainController;

    @FXML
    private TableView<RequestData> allocationTableView;
    @FXML
    private TableColumn<RequestData, String> simulationNameColumn;

    @FXML
    private TableColumn<RequestData, String> usernameColumn;

    @FXML
    private TableColumn<RequestData, Number> tokensColumn;

    @FXML
    private TableColumn<RequestData, String> conditionsColumn;
    private Map<Integer, RequestData> requestDataMap;

    private AllocationRefresher allocationRefresher;

    private Timer timer;

    @FXML
    private HBox hBoxControls;

    @FXML
    private Button buttonDeny;

    @FXML
    private Button buttonAccept;

    @FXML
    public void initialize() {
        requestDataMap = new HashMap<>();
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        simulationNameColumn.setCellValueFactory(new PropertyValueFactory<>("simulationName"));
        tokensColumn.setCellValueFactory(new PropertyValueFactory<>("tokens"));
        conditionsColumn.setCellValueFactory(new PropertyValueFactory<>("endingConditionsFormatted"));
        startRequestTableRefresher();
    }

    public void setMainController(MenuComponentController controller) {
        this.mainController = controller;
    }

    @FXML
    void onAcceptClicked(ActionEvent event) {
        RequestData selectedRequest = allocationTableView.getSelectionModel().getSelectedItem();

        if(selectedRequest != null) {
            AdminServerAgent.changeRequestStatus(this, selectedRequest.getRequestId(), "APPROVED");
            allocationTableView.getItems().remove(selectedRequest);
            requestDataMap.remove(selectedRequest.getRequestId());
        }
    }

    @FXML
    void onDenyClicked(ActionEvent event) {
        RequestData selectedRequest = allocationTableView.getSelectionModel().getSelectedItem();

        if(selectedRequest != null) {
            AdminServerAgent.changeRequestStatus(this, selectedRequest.getRequestId(), "DENIED");
            allocationTableView.getItems().remove(selectedRequest);
            requestDataMap.remove(selectedRequest.getRequestId());
        }
    }

    @FXML
    void onItemSelected(ContextMenuEvent event) {

    }

    public void updateAllocationTableView(DTORequests dtoRequests) {
        for(DTOSingleRequest dtoSingleRequest : dtoRequests.getRequests()) {
            if(!requestDataMap.containsKey(dtoSingleRequest.getReqId())) {
                RequestData requestData = new RequestData(dtoSingleRequest);

                requestDataMap.put(dtoSingleRequest.getReqId(), requestData);
                allocationTableView.getItems().add(requestData);
                showMessageInNotificationBar("New request received from the server");
                removeDisable();
            }
        }
    }

    private void removeDisable() {
        hBoxControls.setDisable(false);
    }

    public void startRequestTableRefresher() {
        allocationRefresher = new AllocationRefresher(this);
        timer = new Timer();
        timer.schedule(allocationRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}