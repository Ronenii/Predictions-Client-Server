package gui.app.menu.request.create.request;

import gui.app.menu.request.RequestComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class NewRequestComponentController {
    private RequestComponentController mainController;
    @FXML
    private TextField simulationsTokensTF;

    @FXML
    private TextField secondsTerminationTF;

    @FXML
    private TextField ticksTerminationTF;

    @FXML
    private Button submitBTN;

    @FXML
    private CheckBox ticksTerminationCB;

    @FXML
    private CheckBox secondsTerminationCB;

    @FXML
    private CheckBox userTerminationCB;

    @FXML
    private ComboBox<?> simulationNameCB;

    public void setMainController(RequestComponentController requestComponentController) {
        this.mainController = requestComponentController;
    }

    @FXML
    void simulationNameCbActionListener(ActionEvent event) {

    }

    @FXML
    void submitButtonActionListener(ActionEvent event) {

    }

}
