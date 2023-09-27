package gui.app.menu.managment.simulation;

import gui.app.menu.managment.ManagementComponentController;
import gui.app.menu.managment.simulation.data.LoadedSimulationData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class SimulationManagerComponentController {

    private ManagementComponentController mainController;

    @FXML
    private TextField pathTF;

    @FXML
    private Button loadFileBTN;

    @FXML
    private ListView<LoadedSimulationData> listViewSimulations;

    public void setMainController(ManagementComponentController managementComponentController){
        this.mainController = managementComponentController;
    }

    @FXML
    void loadFileButtonActionListener(ActionEvent event) {

    }

    @FXML
    void loadFileTextFieldListener(ActionEvent event) {

    }

    @FXML
    void onSimulationSelected(ContextMenuEvent event) {

    }

    @FXML
    void resetTextOnMouseEnteredTextFieldListener(MouseEvent event) {

    }

    @FXML
    void resetTextOnMouseExitTextFieldListener(MouseEvent event) {

    }

}
