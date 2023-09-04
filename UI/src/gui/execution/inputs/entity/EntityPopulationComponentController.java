package gui.execution.inputs.entity;

import gui.execution.inputs.InputsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class EntityPopulationComponentController {

    private InputsController mainController;
    @FXML
    private TextField populationTF;

    @FXML
    private Button setBTN;

    @FXML
    private ListView<?> entitiesLW;

    @FXML
    private Label entityLabel;

    public void setMainController(InputsController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void populationTextFieldActionListener(ActionEvent event) {

    }

    @FXML
    void setButtonActionListener(ActionEvent event) {

    }

}