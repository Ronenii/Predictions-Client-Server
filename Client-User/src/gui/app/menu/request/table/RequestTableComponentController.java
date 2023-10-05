package gui.app.menu.request.table;

import gui.app.menu.request.RequestComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RequestTableComponentController {
    private RequestComponentController mainController;

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

    public void setMainController(RequestComponentController mainController) {
        this.mainController = mainController;
    }
}
