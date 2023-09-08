package gui.execution.inputs.env.var;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTOEnvironmentVariable;
import engine2ui.simulation.prview.PreviewData;
import gui.execution.inputs.InputsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import jaxb.event.FileLoadedEvent;

public class EnvironmentVariablesComponentController implements FileLoadedEvent {

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

    public void setMainController(InputsController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void setButtonActionListener(ActionEvent event) {

    }

    @FXML
    void valueTextFieldActionListener(ActionEvent event) {

    }

    @Override
    public void onFileLoaded(PreviewData previewData) {
        envVarsLV.getItems().addAll(previewData.getEnvVariables());

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
}