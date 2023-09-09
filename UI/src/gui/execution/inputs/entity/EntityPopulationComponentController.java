package gui.execution.inputs.entity;

import engine2ui.simulation.execution.SetResponse;
import engine2ui.simulation.genral.impl.objects.DTOEntity;
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
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;

import java.util.HashMap;
import java.util.Map;

public class EntityPopulationComponentController implements FileLoadedEvent, BarNotifier, EngineCommunicator {

    private static final int POPULATION_ERROR = -1;
    private BarNotifier notificationBar;
    private InputsController mainController;

    @FXML
    private TextField populationTF;

    @FXML
    private Button setBTN;

    @FXML
    private ListView<DTOEntity> entitiesLV;

    @FXML
    private Label entityLabel;

    private Map<DTOEntity, Integer> entityPopulations;

    public void setMainController(InputsController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        entityPopulations = new HashMap<>();
    }

    /**
     * Sets the input text box's text to the population of the current entity selected (if it exists).
     */
    @FXML
    void onMouseClickedListenerLV(MouseEvent event) {
        DTOEntity selectedItem = entitiesLV.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (entityPopulations.containsKey(selectedItem)) {
                populationTF.setText(entityPopulations.get(selectedItem).toString());
            }
        }
    }

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
    }

    @FXML
    void populationTextFieldActionListener(ActionEvent event) {

    }

    /**
     * Tries to set the selected entity's population to the number given in the text field.
     */
    @FXML
    void setButtonActionListener(ActionEvent event) {
        DTOEntity selectedItem = entitiesLV.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            int population = getTextFieldInput();

            if (population != POPULATION_ERROR) {
                SetResponse response = getEngineAgent().sendPopulationData(new EntityPopulationUserInput(selectedItem.getName(), population));
                notificationBar.showNotification(response.getMessage());
                if (response.isSuccess()) {
                    entityPopulations.put(selectedItem, population);
                }
            } else {
                notificationBar.showNotification("The population value may only be a non negative Integer.");
            }
        }
    }

    private int getTextFieldInput() {
        try {
            int result = Integer.parseInt(populationTF.getText());
            if (result < 0) {
                throw new Exception();
            }
            return result;
        } catch (Exception e) {
            return POPULATION_ERROR;
        }
    }

    @Override
    public void onFileLoaded(PreviewData previewData) {
        entitiesLV.getItems().addAll(previewData.getEntities());

        entitiesLV.setCellFactory(new Callback<ListView<DTOEntity>, ListCell<DTOEntity>>() {
            @Override
            public ListCell<DTOEntity> call(ListView<DTOEntity> listView) {
                return new ListCell<DTOEntity>() {
                    @Override
                    protected void updateItem(DTOEntity item, boolean empty) {
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
}