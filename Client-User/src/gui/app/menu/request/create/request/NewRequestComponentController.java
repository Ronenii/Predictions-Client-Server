package gui.app.menu.request.create.request;

import gui.api.Controller;
import gui.app.menu.request.RequestComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.prview.SimulationsPreviewData;


public class NewRequestComponentController implements Controller {
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
        if(simulationNameCB.getSelectionModel().getSelectedItem() == null) {
            showMessageInNotificationBar("Please choose a simulation from the list");
        }else if (!isTokenTextFieldValid()) {
            showMessageInNotificationBar("Please enter a number for the simulation tokens");
        } else if(!ticksTerminationCB.isSelected() && !secondsTerminationCB.isSelected() && !userTerminationCB.isSelected()) {
            showMessageInNotificationBar("Please choose at least one ending condition");
        } else if(userTerminationCB.isSelected() && (ticksTerminationCB.isSelected() || secondsTerminationCB.isSelected())) {
            showMessageInNotificationBar("You can not choose 'By user' ending condition and 'By ticks'/'By seconds' ending condition");
        } else {
            createRequestDTOAndSendToTheServer();
            showMessageInNotificationBar("New request has been sent!");
        }
    }

    private void createRequestDTOAndSendToTheServer() {
        // TODO: this method will send the request. Receive the requestID, update this ID, and the Request table.
    }

    private boolean isTokenTextFieldValid() {
        boolean ret = false;
        String tokens = simulationsTokensTF.getText();

        if(tokens != null) {
            try {
                Integer.parseInt(tokens);
                ret = true;
            } catch (NumberFormatException e) {
                // no use for the exception catch.
            }
        }

        return ret;
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

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
