package gui.app.menu.request.create.request;

import gui.app.menu.request.RequestComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.prview.SimulationsPreviewData;

import java.util.Arrays;

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
    private ComboBox<String> simulationNameCB;

    public void setMainController(RequestComponentController requestComponentController) {
        this.mainController = requestComponentController;
    }

    @FXML
    void simulationNameCbActionListener(ActionEvent event) {

    }

    @FXML
    void submitButtonActionListener(ActionEvent event) {

    }

    @FXML
    void ticksTerminationCBActionListener(ActionEvent event) {
        if(ticksTerminationCB.isSelected()){
            ticksTerminationTF.setDisable(false);
        } else {
            ticksTerminationTF.setDisable(true);
        }
    }


    @FXML
    void secondsTerminationCBActionListener(ActionEvent event) {
        if(secondsTerminationCB.isSelected()){
            secondsTerminationTF.setDisable(false);
        } else {
            secondsTerminationTF.setDisable(true);
        }
    }

    public void updateNewRequestComponent(SimulationsPreviewData simulationsPreviewData) {
        for(PreviewData previewData : simulationsPreviewData.getPreviewDataArray()) {
            simulationNameCB.getItems().add(previewData.getSimulationName());
        }
    }

}
