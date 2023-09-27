package gui.app.menu.allocation;

import gui.app.menu.MenuComponentController;
import gui.app.menu.allocation.data.RequestData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
public class AllocationComponentController {

    private MenuComponentController mainController;

    @FXML
    private TableView<RequestData> allocationTableView;

    @FXML
    private HBox hBoxControls;

    @FXML
    private Button buttonDeny;

    @FXML
    private Button buttonAccept;

    public void setMainController(MenuComponentController menuComponentController){
        mainController = menuComponentController;
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
}