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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    private Set<String> simulationNameSet = new HashSet<>();

    public void setMainController(RequestComponentController requestComponentController) {
        this.mainController = requestComponentController;
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
            showMessageInNotificationBar("You can not choose 'By user' ending condition with 'By ticks'/'By seconds' ending condition");
        } else if(!isTicksOrSecondTextFieldValid()) {
            showMessageInNotificationBar("Please enter a number for the ticks/seconds ending conditions");
        } else {
            createRequestDTOAndSendToTheServer();
        }
    }


    private void createRequestDTOAndSendToTheServer() {
        DTORequest dtoRequest = createDTORequest();
        UserServerAgent.sendSimulationRequest(this, dtoRequest);
    }

    /**
     * 'createRequestDTOAndSendToTheServer' helper, this method creates a DTORequest object from the JavaFX TextFields.
     */
    private DTORequest createDTORequest() {
        String simulationName = simulationNameCB.getSelectionModel().getSelectedItem();
        int simulationTokens = Integer.parseInt(simulationsTokensTF.getText());
        DTOEndingCondition[] endingConditions = createDTOEndingConditionArray();

        return new DTORequest(simulationName, simulationTokens, endingConditions);
    }

    /**
     * 'createDTORequest' helper.
     */
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

    /**
     * Check if the ticks or second CB is checked, if yes, using 'isTextFieldValid' to validate their TextFields.
     */
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

    /**
     * Validate the given TextField's text to be a number.
     */
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

    /**
     * After receiving new SimulationsPreviewData from the server, this method receive the SimulationsPreviewData object
     * and update the new request component.
     */
    public void updateNewRequestComponent(SimulationsPreviewData simulationsPreviewData) {
        for(PreviewData previewData : simulationsPreviewData.getPreviewDataArray()) {
            if(!simulationNameSet.contains(previewData.getSimulationName())) {
                simulationNameSet.add(previewData.getSimulationName());
                simulationNameCB.getItems().add(previewData.getSimulationName());
            }
        }
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
