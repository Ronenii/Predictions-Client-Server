package gui.result.tab;

import gui.result.ResultComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

public class ResultTabComponentController {
    private ResultComponentController mainController;
    @FXML
    private Label executionResultLabel;

    @FXML
    private TabPane executionResultTP;

    public void setMainController(ResultComponentController mainController) {
        this.mainController = mainController;
    }

    public void enableResultComponent() {
        executionResultLabel.disableProperty().set(false);
        executionResultTP.disableProperty().set(false);
    }

    public void disableResultComponent() {
        executionResultLabel.disableProperty().set(true);
        executionResultTP.disableProperty().set(true);
    }


    // TODO: Implement both of these
    private void loadResultComponent() {
        unloadResultComponent();
    }

    private void unloadResultComponent() {

    }
}
