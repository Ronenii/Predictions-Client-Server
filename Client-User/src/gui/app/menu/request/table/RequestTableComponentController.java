package gui.app.menu.request.table;

import client2server.simulation.request.DTORequest;
import gui.api.Controller;
import gui.app.menu.request.RequestComponentController;
import gui.app.menu.request.data.RequestData;
import gui.app.menu.request.table.refresher.RequestsStatusRefresher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import manager.constants.Constants;
import server2client.simulation.request.updated.status.DTORequestStatusUpdate;
import server2client.simulation.request.updated.status.data.DTORequestStatusData;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

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

    private RequestsStatusRefresher requestsStatusRefresher;
    private Timer timer;

    @FXML
    public void initialize() {
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        simulationNameColumn.setCellValueFactory(new PropertyValueFactory<>("simulationName"));
        tokensColumn.setCellValueFactory(new PropertyValueFactory<>("tokens"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        runningColumn.setCellValueFactory(new PropertyValueFactory<>("running"));
        finishedColumn.setCellValueFactory(new PropertyValueFactory<>("finished"));
        requestsStatusRefresher = null;
    }

    @FXML
    void executeButtonActionListener(ActionEvent event) {
        RequestData selectedRequest = requestsTV.getSelectionModel().getSelectedItem();

        if(selectedRequest != null) {
            if(selectedRequest.getStatus().equals("PENDING")) {
                showMessageInNotificationBar(String.format("Request #%d has not been approved yet", selectedRequest.getRequestId()));
            } else if(selectedRequest.getStatus().equals("DENIED")){
                showMessageInNotificationBar(String.format("Request #%d denied by the admin", selectedRequest.getRequestId()));
            } else {
                mainController.moveToExecutionSetUp(selectedRequest);
            }
        }
    }

    /**
     * This method receive a new request's DTO from the new request component and the request ID from the server, create new RequestData and insert the request to the table.
     */
    public void addNewRequestData(int requestId, DTORequest dtoRequest) {
        RequestData newRequestData = new RequestData(requestId, dtoRequest);
        requestDataMap.put(requestId, newRequestData);
        requestsTV.getItems().add(newRequestData);
        startRequestsStatusRefresher();
    }

    public void refreshRequestsTv(){
        requestsTV.refresh();
    }

    /**
     * Initialize the RequestStatusRefresher if it has not been initialized yet.
     */
    private void startRequestsStatusRefresher() {
        // enable the timer task only once.
        if(requestsStatusRefresher == null) {
            requestsStatusRefresher = new RequestsStatusRefresher(this);
            timer = new Timer();
            timer.schedule(requestsStatusRefresher, Constants.REQUEST_TABLE_REFRESH_RATE, Constants.REQUEST_TABLE_REFRESH_RATE);
        }
    }

    /**
     * This method called from the server's response to the 'requestsStatusRefresher' with a 'DTORequestStatusUpdate' object.
     * If any request's status has been changed on the server, this method change this request status in the table as well.
     */
    public void updateRequestsStatus(DTORequestStatusUpdate dtoRequestStatusUpdate) {
        for(DTORequestStatusData dtoRequestStatusData : dtoRequestStatusUpdate.getRequestStatusUpdates()) {
            RequestData requestData = requestDataMap.get(dtoRequestStatusData.getReqId());
            requestData.setStatus(dtoRequestStatusData.getReqStatus());
            requestsTV.refresh();
            showMessageInNotificationBar(String.format("New response for request #%d from the admin", requestData.getRequestId()));
        }
    }

    public void setMainController(RequestComponentController mainController) {
        this.mainController = mainController;
    }

    public RequestData getRequestDataById(int reqId){
        return requestDataMap.get(reqId);
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
