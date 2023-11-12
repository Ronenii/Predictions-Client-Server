package gui.app.menu.request.create.request;

import client2server.simulation.request.DTORequest;
import gui.api.Controller;
import gui.app.menu.request.RequestComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import manager.UserServerAgent;
import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.prview.SimulationsPreviewData;

import java.util.ArrayList;
import java.util.List;


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
        }else if (!isTextFieldValid(simulationsTokensTF)) {
            showMessageInNotificationBar("Please enter a number for the simulation tokens");
        } else if(!ticksTerminationCB.isSelected() && !secondsTerminationCB.isSelected() && !userTerminationCB.isSelected()) {
            showMessageInNotificationBar("Please choose at least one ending condition");
        } else if(userTerminationCB.isSelected() && (ticksTerminationCB.isSelected() || secondsTerminationCB.isSelected())) {
            showMessageInNotificationBar("You can not choose 'By user' ending condition and 'By ticks'/'By seconds' ending condition");
        } else if(!isTicksOrSecondTextFieldValid()) {
            showMessageInNotificationBar("Please enter a number for the ticks/seconds ending conditions");
        } else {
            createRequestDTOAndSendToTheServer();
            showMessageInNotificationBar("New request has been sent!");
        }
    }

    private void createRequestDTOAndSendToTheServer() {
        // TODO: this method will send the request. Receive the requestID, update this ID, and the Request table.
        DTORequest dtoRequest = createDTORequest();
        UserServerAgent.sendSimulationRequest(this, dtoRequest);
    }

    private DTORequest createDTORequest() {
        String simulationName = simulationNameCB.getSelectionModel().getSelectedItem();
        int simulationTokens = Integer.parseInt(simulationsTokensTF.getText());
        DTOEndingCondition[] endingConditions = createDTOEndingConditionArray();

        return new DTORequest(simulationName, simulationTokens, endingConditions);
    }

    private DTOEndingCondition[] createDTOEndingConditionArray() {
        List<DTOEndingCondition> tempList = new ArrayList<>();

        if(ticksTerminationCB.isSelected()) {
            tempList.add(new DTOEndingCondition("Ticks", Integer.parseInt(ticksTerminationTF.getText())));
        }
        if(secondsTerminationCB.isSelected()) {
            tempList.add(new DTOEndingCondition("Seconds", Integer.parseInt(secondsTerminationTF.getText())));
        }
        if(userTerminationCB.isSelected()) {
            tempList.add(new DTOEndingCondition("User", -1));
        }

        return tempList.toArray(new DTOEndingCondition[0]);
    }

    private boolean isTicksOrSecondTextFieldValid() {
        boolean ret = true;

        if(ticksTerminationCB.isSelected() && !isTextFieldValid(ticksTerminationTF)) {
            ret = false;
        }
        if(secondsTerminationCB.isSelected() && !isTextFieldValid(secondsTerminationTF)) {
            ret = false;
        }

        return ret;
    }

    private boolean isTextFieldValid(TextField textField) {
        boolean ret = false;
        String textFieldText = textField.getText();

        if(textFieldText != null) {
            try {
                Integer.parseInt(textFieldText);
                ret = true;
            } catch (NumberFormatException e) {
                // no use for the exception catch.
            }
        }

        return ret;
    }

    public void addNewRequestData(int requestId, DTORequest dtoRequest) {
        mainController.addNewRequestData(requestId, dtoRequest);
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
