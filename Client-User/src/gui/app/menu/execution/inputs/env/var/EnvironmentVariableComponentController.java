package gui.app.menu.execution.inputs.env.var;

import gui.api.Controller;
import server2client.simulation.execution.SetResponse;
import server2client.simulation.genral.impl.properties.DTOEnvironmentVariable;
import server2client.simulation.prview.PreviewData;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.app.menu.execution.inputs.InputsController;
import gui.app.menu.execution.models.EnvironmentVarsStartData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import jaxb.event.FileLoadedEvent;
import manager.UserServerAgent;
import simulation.properties.property.api.PropertyType;
import client2server.simulation.execution.user.input.EnvPropertyUserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvironmentVariableComponentController implements UserEngineCommunicator, Controller {

    private InputsController mainController;
    @FXML
    private TextField valueTF;

    @FXML
    private Button setBTN;

    @FXML
    private Label explanationLabel;

    @FXML
    private ListView<DTOEnvironmentVariable> envVarsLV;

    @FXML
    private Label envVarLabel;

    private Map<DTOEnvironmentVariable, String> environmentVariableMap; // Used for updating the TF values.

    public void setMainController(InputsController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        environmentVariableMap = new HashMap<>();
    }

    @FXML
    void onMouseClickedListenerLV(MouseEvent event) {
        DTOEnvironmentVariable selectedItem = envVarsLV.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            changeExplanationPrompt(selectedItem);
            updateTextFieldToCurrentEnvVarValue(selectedItem);
        }
    }

    /**
     * Changes the explanation label's text to a brief explanation for the currently selected env var type
     */
    private void changeExplanationPrompt(DTOEnvironmentVariable environmentVariable) {
        String prompt = "";
        String range = String.format("Accepted values are %s-%s.\n", environmentVariable.getFrom(), environmentVariable.getTo());

        switch (PropertyType.valueOf(environmentVariable.getType().toUpperCase())) {
            case DECIMAL:
                prompt = "Integer.\n";
                break;
            case FLOAT:
                prompt = "Float.\n";
                break;
            case BOOLEAN:
                prompt = "Boolean.\n";
                range = "Accepted values are true or false.\n";
                break;
            case STRING:
                prompt = "String.\n";
                range = "Up to 50 chars, accepted chars:\n" +
                        "-lower and upper case letters.\n" +
                        "-numbers: 0-9\n" +
                        "-special characters: 'space'!,?_-.()\n";
                break;
        }
        explanationLabel.setText(prompt + range + "Leave empty for random value.");
    }

    /**
     * Updates the value text field to the current environment variable's user given value.
     */
    private void updateTextFieldToCurrentEnvVarValue(DTOEnvironmentVariable environmentVariable){
        valueTF.setText(environmentVariableMap.getOrDefault(environmentVariable, ""));
    }

    @FXML
    void setButtonActionListener(ActionEvent event) {
        DTOEnvironmentVariable selectedItem = envVarsLV.getSelectionModel().getSelectedItem();

        // If there is in fact, a selected item in the list view.
        if (selectedItem != null && !valueTF.getText().equals(environmentVariableMap.get(selectedItem))) {
            SetResponse response;

            // If the text field is empty then the environment variable is randomly initialized
            // Else its user initialized.
            if (valueTF.getText().isEmpty()) {
                response = getEngineAgent().sendEnvironmentVariableData(new EnvPropertyUserInput(selectedItem.getName(), true, null));
            } else {
                response = getEngineAgent().sendEnvironmentVariableData(new EnvPropertyUserInput(selectedItem.getName(), false, valueTF.getText()));
            }

            showMessageInNotificationBar(response.getMessage());

            if(response.isSuccess()){
                environmentVariableMap.put(selectedItem, valueTF.getText());
            }
        }
    }

    /**
     * Makes the text field to activate the set button if enter was pressed while text field is selected.
     */
    @FXML
    void valueTextFieldActionListener(ActionEvent event) {
        setButtonActionListener(event);
    }

    public void loadEnvVarsDetails(PreviewData previewData) {
        valueTF.clear();
        clearListView();
        enableComponent();
        addItemsToListView(previewData.getEnvVariables());
        // TODO : check if the following method is needed.
        //initEnvironmentVariables(previewData.getEnvVariables());
    }

    private void clearListView(){
        envVarsLV.getItems().clear();
    }

    /**
     * Sets All environment variables by default to be random initialized.
     */
    private void initEnvironmentVariables(DTOEnvironmentVariable[] envVariables) {
        for (DTOEnvironmentVariable e : envVariables) {
            getEngineAgent().sendEnvironmentVariableData(new EnvPropertyUserInput(e.getName(), true, null));
        }
    }

    /**
     * Enables all components of this component.
     */
    private void enableComponent() {
        setBTN.disableProperty().set(false);
        envVarsLV.disableProperty().set(false);
        valueTF.disableProperty().set(false);
    }


    private void addItemsToListView(DTOEnvironmentVariable[] envVariables) {
        envVarsLV.getItems().addAll(envVariables);
        for (DTOEnvironmentVariable environmentVariable : envVariables) {
            environmentVariableMap.put(environmentVariable,"");
        }

        envVarsLV.setCellFactory(new Callback<ListView<DTOEnvironmentVariable>, ListCell<DTOEnvironmentVariable>>() {
            @Override
            public ListCell<DTOEnvironmentVariable> call(ListView<DTOEnvironmentVariable> listView) {
                return new ListCell<DTOEnvironmentVariable>() {
                    @Override
                    protected void updateItem(DTOEnvironmentVariable item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };

            }
        });
    }

    @Override
    public UserServerAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    /**
     * Deselects the listview and sets the valueTF to be empty.
     */
    private void resetListView(){
        envVarsLV.getSelectionModel().clearSelection();
        valueTF.setText("");
    }

    /**
     * Assigns all env vars random values and resets the map.
     */
    public void clearInputs(){
        environmentVariableMap.replaceAll((e, v) -> "");
        initEnvironmentVariables(environmentVariableMap.keySet().toArray(new DTOEnvironmentVariable[0]));
        resetListView();
    }

    public void fetchEnvironmentVarsStartData(EnvironmentVarsStartData environmentVarsStartData) {
        Map<String, Object> envVarsValuesMap = environmentVarsStartData.getEnvVarsValuesMap();

        for(DTOEnvironmentVariable environmentVariable : environmentVariableMap.keySet()) {
            environmentVariableMap.put(environmentVariable, envVarsValuesMap.get(environmentVariable.getName()).toString());
        }

        for (DTOEnvironmentVariable environmentVariable : environmentVariableMap.keySet()) {
            if (environmentVariableMap.get(environmentVariable) == null) {
                getEngineAgent().sendEnvironmentVariableData(new EnvPropertyUserInput(environmentVariable.getName(), true, null));
            } else {
                getEngineAgent().sendEnvironmentVariableData(new EnvPropertyUserInput(environmentVariable.getName(), false, environmentVariableMap.get(environmentVariable)));
            }
        }

        DTOEnvironmentVariable selectedItem = envVarsLV.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            updateTextFieldToCurrentEnvVarValue(selectedItem);
        }
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}