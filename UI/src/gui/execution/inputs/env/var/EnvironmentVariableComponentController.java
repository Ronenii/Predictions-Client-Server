package gui.execution.inputs.env.var;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTOEnvironmentVariable;
import engine2ui.simulation.prview.PreviewData;
import gui.api.BarNotifier;
import gui.api.EngineCommunicator;
import gui.execution.inputs.InputsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import jaxb.event.FileLoadedEvent;
import manager.EngineAgent;
import simulation.properties.property.api.PropertyType;
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;
import ui2engine.simulation.execution.user.input.EnvPropertyUserInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvironmentVariableComponentController implements FileLoadedEvent, EngineCommunicator, BarNotifier {

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

    private Map<DTOEnvironmentVariable, String> envVarsValues;

    public void setMainController(InputsController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        envVarsValues = new HashMap<>();
    }

    @FXML
    void onMouseClickerdListenerLV(MouseEvent event) {
        DTOEnvironmentVariable selectedItem = envVarsLV.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            changeExplanationPrompt(selectedItem);
        }
    }

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

    @FXML
    void setButtonActionListener(ActionEvent event) {

    }

    @FXML
    void valueTextFieldActionListener(ActionEvent event) {
        setButtonActionListener(event);
    }

    @Override
    public void onFileLoaded(PreviewData previewData) {
        enableComponent();
        addItemsToListView(previewData.getEnvVariables());
        initEnvironmentVariables(previewData.getEnvVariables());
    }

    private void initEnvironmentVariables(List<DTOEnvironmentVariable> envVariables) {
        for (DTOEnvironmentVariable e : envVariables
        ) {
            getEngineAgent().sendEnvironmentVariableData(new EnvPropertyUserInput(e.getName(), true, null));
        }
    }

    private void enableComponent() {
        setBTN.disableProperty().set(false);
        envVarsLV.disableProperty().set(false);
        valueTF.disableProperty().set(false);
    }

    private void addItemsToListView(List<DTOEnvironmentVariable> envVariables) {
        envVarsLV.getItems().addAll(envVariables);

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
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }
}