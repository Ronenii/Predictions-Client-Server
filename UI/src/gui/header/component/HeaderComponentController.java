package gui.header.component;

import gui.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HeaderComponentController {
    private AppController mainController;
    @FXML
    private Button loadFileBTN;
    @FXML
    private Button queueManBTN;
    @FXML
    private TextField pathTF;
    @FXML
    private Label predictionLabel;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void loadFileButtonActionListener(ActionEvent event) {

    }

    @FXML
    void queueManButtonActionListener(ActionEvent event) {

    }

}