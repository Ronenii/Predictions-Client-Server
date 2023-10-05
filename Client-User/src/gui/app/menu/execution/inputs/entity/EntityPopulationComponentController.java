package gui.app.menu.execution.inputs.entity;

import engine2ui.simulation.execution.SetResponse;
import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTOGridAndThread;
import engine2ui.simulation.prview.PreviewData;
import gui.api.BarNotifier;
import gui.api.UserEngineCommunicator;
import gui.app.menu.execution.inputs.InputsController;
import gui.app.menu.execution.models.EntitiesStartData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import jaxb.event.FileLoadedEvent;
import manager.UserEngineAgent;
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityPopulationComponentController implements FileLoadedEvent, BarNotifier, UserEngineCommunicator {

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

    @Override
    public BarNotifier getNotificationBar() {
        return mainController.getNotificationBar();
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
                    SetResponse response = getEngineAgent().sendPopulationData(new EntityPopulationUserInput(selectedItem.getName(), population));
                    getNotificationBar().showNotification(response.getMessage());
                    if (response.isSuccess()) {
                        updateEntityCounter(selectedItem, population);
                        entityPopulations.put(selectedItem, population);
                    }
                }
            } else {
                getNotificationBar().showNotification("ERROR: The population value may only be a non negative Integer.");
            }
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

    @Override
    public void onFileLoaded(PreviewData previewData, boolean isFirstSimulationLoaded) {
        populationTF.clear();
        clearListView();
        enableComponent();
        addItemsToListView(previewData.getEntities());
        initEntityPopulations(previewData.getEntities());
        updateEntitiesLeft(calcGridCells(previewData.getGridAndThread()));
    }

    private void updateEntitiesLeft(int entityCount){
        entitiesLeftToAdd = entityCount;
        entitiesLeftLabel.setText(String.valueOf(entityCount));
    }

    private int calcGridCells(DTOGridAndThread grid){
        gridSize = grid.getGridRows() * grid.getGridColumns();
        return gridSize;
    }

    /**
     * Sets all entity populations to 0.
     * Both in the UI and in the
     *
     * @param entities
     */
    private void initEntityPopulations(List<DTOEntity> entities) {
        updateEntitiesLeft(gridSize);
        for (DTOEntity e : entities
        ) {
            getEngineAgent().sendPopulationData(new EntityPopulationUserInput(e.getName(), 0));
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


    private void addItemsToListView(List<DTOEntity> entities) {
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
        initEntityPopulations(new ArrayList<>(entityPopulations.keySet()));
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
    public UserEngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public EntitiesStartData getEntitiesStartData() {
        Map<DTOEntity, Integer> entityPopulationsDup = new HashMap<>();

        for (DTOEntity entity : entityPopulations.keySet()) {
            entityPopulationsDup.put(entity, entityPopulations.get(entity));
        }

        return new EntitiesStartData(entityPopulationsDup, entitiesLeftToAdd);
    }

    public void fetchEntitiesStartData(EntitiesStartData entitiesStartData) {
        for (DTOEntity entity : entitiesStartData.getEntityPopulations().keySet()) {
            entityPopulations.put(entity, entitiesStartData.getEntityPopulations().get(entity));
        }

        if (entitiesLV.getSelectionModel().getSelectedItem() != null) {
            populationTF.setText(entityPopulations.get(entitiesLV.getSelectionModel().getSelectedItem()).toString());
        }

        entitiesLeftToAdd = entitiesStartData.getEntitiesLeft();
        entitiesLeftLabel.setText(String.valueOf(entitiesLeftToAdd));
        for (DTOEntity entity : entityPopulations.keySet()) {
            getEngineAgent().sendPopulationData(new EntityPopulationUserInput(entity.getName(), entityPopulations.get(entity)));
        }
    }
}