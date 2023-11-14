package gui.app.menu.allocation;

import gui.app.api.Controller;
import gui.app.menu.MenuComponentController;
import gui.app.menu.allocation.data.RequestData;
import gui.app.menu.allocation.refresher.AllocationRefresher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import manager.AdminServerAgent;
import manager.constant.Constants;

import java.util.Timer;

public class AllocationComponentController implements Controller {

    private Controller mainController;

    @FXML
    private TableView<RequestData> allocationTableView;

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
        startRequestTableRefresher();
    }

    public void setMainController(MenuComponentController controller) {
        this.mainController = controller;
    }

    @FXML
    void onAcceptClicked(ActionEvent event) {

    }

    @FXML
    void onDenyClicked(ActionEvent event) {

    }

    @FXML
    void onItemSelected(ContextMenuEvent event) {

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