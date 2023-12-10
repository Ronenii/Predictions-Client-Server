package gui.app.menu.execution.inputs.entity;

import gui.api.Controller;
import server2client.simulation.execution.SetResponse;
import server2client.simulation.genral.impl.objects.DTOEntity;
import server2client.simulation.genral.impl.properties.DTOGrid;
import server2client.simulation.prview.PreviewData;
import gui.api.UserEngineCommunicator;
import gui.app.menu.execution.inputs.InputsController;
import gui.app.menu.execution.models.EntitiesStartData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import manager.UserServerAgent;
import client2server.simulation.execution.user.input.EntityPopulationUserInput;

import java.util.HashMap;
import java.util.Map;

public class EntityPopulationComponentController implements Controller, UserEngineCommunicator {

    private static final int POPULATION_ERROR = -1, NO_POPULATION = -1;
    public Label entitiesLeftLabel;
    private InputsController mainController;

    @FXML
    private TextField populationTF;

    @FXML
    private Button setBTN;

    @FXML
    private ListView<DTOEntity> entitiesLV;

    @FXML
    private Label entityLabel;
    private int entitiesLeftToAdd;

    private int gridSize;

    private Map<DTOEntity, Integer> entityPopulations; // Used for updating the TF values.

    public void setMainController(InputsController mainController) {
        this.mainController = mainController;
    }

    @FXML
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

    @FXML
    void populationTextFieldActionListener(ActionEvent event) {
        setButtonActionListener(event);
    }

    /**
     * Tries to set the selected entity's population to the number given in the text field.
     */
    @FXML
    void setButtonActionListener(ActionEvent event) {
        DTOEntity selectedItem = entitiesLV.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            int population = parseTextFieldInput();

            if (population != POPULATION_ERROR) {
                if (population != entityPopulations.get(selectedItem)) {
                    UserServerAgent.sendPopulationData(this, new EntityPopulationUserInput(mainController.getCurrentReqId(), selectedItem.getName(), population), selectedItem, false);
                }
            } else {
                showMessageInNotificationBar("ERROR: The population value may only be a non negative Integer.");
            }
        }
    }

    public void receiveSetResponse(SetResponse response, DTOEntity selectedItem, int population) {
        showMessageInNotificationBar(response.getMessage());
        if (response.isSuccess()) {
            updateEntityCounter(selectedItem, population);
            entityPopulations.put(selectedItem, population);
        }
    }

    /**
     * Checks if the entity we updated already has a population, if so then updates the counter with the new population value given.
     * @param entity The entity we update/
     * @param population The population we added. This is given after validation so there is no need to validate it.
     */
    private void updateEntityCounter(DTOEntity entity, int population){
        if(entityPopulations.containsKey(entity)){
            entitiesLeftToAdd += entityPopulations.get(entity);
        }
        entitiesLeftToAdd -= population;
        entitiesLeftLabel.setText(String.valueOf(entitiesLeftToAdd));
    }

    /**
     * Checks if the given input is in fact a non-negative integer.
     */
    private int parseTextFieldInput() {
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

    public void loadEntitiesDet(PreviewData previewData) {
        populationTF.clear();
        clearListView();
        enableComponent();
        addItemsToListView(previewData.getEntities());
        updateEntitiesLeft(calcGridCells(previewData.getGridAndThread()));
        initEntityPopulations(previewData.getEntities());
    }

    private void updateEntitiesLeft(int entityCount){
        entitiesLeftToAdd = entityCount;
        entitiesLeftLabel.setText(String.valueOf(entityCount));
    }

    private int calcGridCells(DTOGrid grid){
        gridSize = grid.getGridRows() * grid.getGridColumns();
        return gridSize;
    }

    /**
     * Sets all entity populations to 0.
     * Both in the UI and in the
     *
     * @param entities
     */
    private void initEntityPopulations(DTOEntity[] entities) {
        updateEntitiesLeft(gridSize);
        for (DTOEntity e : entities) {
            UserServerAgent.sendPopulationData(this, new EntityPopulationUserInput(mainController.getCurrentReqId(), e.getName(), 0), null, true);
            entityPopulations.put(e, 0);
        }
    }

    private void enableComponent() {
        setBTN.disableProperty().set(false);
        entitiesLV.disableProperty().set(false);
        populationTF.disableProperty().set(false);
    }

    private void clearListView(){
        entitiesLV.getItems().clear();
    }


    private void addItemsToListView(DTOEntity[] entities) {
        entitiesLV.getItems().addAll(entities);
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

    /**
     * Sets all entity populations in the engine to 0 and resets accordingly the entity populations map.
     */
    public void clearInputs() {
        entityPopulations.replaceAll((e, v) -> 0);
        initEntityPopulations(entityPopulations.keySet().toArray(new DTOEntity[0]));
        resetListView();
    }

    /**
     * Deselects the listview and clears the TF.
     */
    private void resetListView() {
        entitiesLV.getSelectionModel().clearSelection();
        populationTF.setText("");
    }

    @Override
    public UserServerAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public EntitiesStartData getEntitiesStartData() {
        Map<DTOEntity, Integer> entityPopulationsDup = new HashMap<>();

        for (DTOEntity entity : entityPopulations.keySet()) {
            entityPopulationsDup.put(entity, entityPopulations.get(entity));
        }

        return new EntitiesStartData(entityPopulationsDup, entitiesLeftToAdd);
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}