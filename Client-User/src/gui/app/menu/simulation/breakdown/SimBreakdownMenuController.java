package gui.app.menu.simulation.breakdown;
import gui.api.Controller;
import gui.app.menu.simulation.breakdown.refresher.SimulationBreakdownRefresher;
import manager.constants.Constants;
import server2client.simulation.genral.impl.objects.DTOEntity;
import server2client.simulation.genral.impl.properties.DTORule;
import server2client.simulation.genral.impl.properties.action.api.DTOAction;
import server2client.simulation.genral.impl.properties.DTOProperty;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.genral.impl.properties.DTOEnvironmentVariable;
import gui.api.HasFileLoadedListeners;
import gui.app.menu.simulation.breakdown.details.DisplayComponentController;
import gui.app.menu.simulation.breakdown.details.entity.property.PropertyDetailsController;
import gui.app.menu.simulation.breakdown.details.environment.EnvironmentVarDetailsController;
import gui.app.menu.simulation.breakdown.details.general.GeneralDetailsController;
import gui.app.menu.simulation.breakdown.details.rule.action.ActionDetailsController;
import gui.app.menu.simulation.breakdown.details.rule.activation.ActivationDetailsController;
import gui.app.menu.MenuComponentController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import jaxb.event.FileLoadedEvent;
import server2client.simulation.prview.SimulationsPreviewData;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SimBreakdownMenuController implements Initializable, HasFileLoadedListeners, FileLoadedEvent, Controller {
    @FXML private GridPane currentMainComponent;
    @FXML private GridPane treeViewGridPane;
    private MenuComponentController mainController;
    @FXML
    private TreeView<String> simTreeView;
    @FXML
    private ScrollPane displayComponent;
    @FXML
    private DisplayComponentController displayComponentController;
    private TreeItem<String> simulationsItem;
    private SimulationBreakdownRefresher simulationBreakdownRefresher;
    private SimulationsPreviewData updatedSimulationsPreviewData;
    private Timer timer;

    public void setMainController(MenuComponentController mainController) {
        this.mainController = mainController;
    }

    public void setPreviewData(PreviewData previewData) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> simulations = new TreeItem<>("Simulations");
        simTreeView.setRoot(simulations);
        this.simulationsItem = simulations;
        this.updatedSimulationsPreviewData = null;
        startSimBreakdownRefresher();
    }

    /**
     * Update the tree view items with the loaded XML file's data.
     */
    public void updateSimTreeView(SimulationsPreviewData simulationsPreviewData) {
        clearTreeView();
        for(PreviewData simPreviewData : simulationsPreviewData.getPreviewDataArray()) {
            updateSimulationInTreeView(simPreviewData);
        }

        mainController.updateNewRequestComponent(simulationsPreviewData);
        showMessageInNotificationBar("New simulations have been loaded");
        this.updatedSimulationsPreviewData = simulationsPreviewData;
    }

    private void updateSimulationInTreeView(PreviewData simPreviewData) {
        TreeItem<String> simulationItem, envVarsItem, entitiesItem, rulesItem, generalItem;

        simulationItem = new TreeItem<>(simPreviewData.getSimulationName());
        envVarsItem = new TreeItem<>("Environment Variables");
        entitiesItem = new TreeItem<>("Entities");
        rulesItem = new TreeItem<>("Rules");
        generalItem = new TreeItem<>("General");
        simulationsItem.getChildren().add(simulationItem);
        simulationItem.getChildren().addAll(envVarsItem, entitiesItem, rulesItem, generalItem);
        updateEnvVarsInTreeView(simPreviewData.getEnvVariables(), envVarsItem);
        updateEntitiesInTreeView(simPreviewData.getEntities(), entitiesItem);
        updateRulesInTreeView(simPreviewData.getRules(), rulesItem);
    }

    /**
     * 'updateSimTreeView' helper - add the environment vars to the tree view.
     */
    private void updateEnvVarsInTreeView(DTOEnvironmentVariable[] envVariables, TreeItem<String> envVarsItem) {
        TreeItem<String> envVarItem;

        for(DTOEnvironmentVariable envVar : envVariables) {
            envVarItem = new TreeItem<>(envVar.getName());
            envVarsItem.getChildren().add(envVarItem);
        }
    }

    /**
     * 'updateSimTreeView' helper - add the entities to the tree view.
     */
    private void updateEntitiesInTreeView(DTOEntity[] entities, TreeItem<String> entitiesItem) {
        TreeItem<String> entityItem;

        for (DTOEntity entity : entities) {
            entityItem = new TreeItem<>(entity.getName());
            updateEntityPropertiesInTreeView(entityItem, entity.getProperties());
            entitiesItem.getChildren().add(entityItem);
        }
    }

    /**
     * 'updateSimTreeView' helper - add entity's properties to the tree view.
     */
    private void updateEntityPropertiesInTreeView(TreeItem<String> entityItem, DTOProperty[] entityProp) {
        TreeItem<String> propertyItem;

        for (DTOProperty property : entityProp) {
            propertyItem = new TreeItem<>(property.getName());
            entityItem.getChildren().add(propertyItem);
        }
    }

    /**
     * 'updateSimTreeView' helper - add the rules to the tree view.
     */
    private void updateRulesInTreeView(DTORule[] rules, TreeItem<String> rulesItem) {
        TreeItem<String> ruleItem;

        for (DTORule rule : rules) {
            ruleItem = new TreeItem<>(rule.getName());
            updateRuleActionsInTreeView(ruleItem, rule.getActions());
            TreeItem<String> activationItem = new TreeItem<>("Activation");
            ruleItem.getChildren().add(activationItem);
            rulesItem.getChildren().add(ruleItem);
        }
    }

    /**
     * 'updateSimTreeView' helper - add the rules actions to the tree view.
     */
    private void updateRuleActionsInTreeView(TreeItem<String> ruleItem, DTOAction[] actions) {
        TreeItem<String> actionsItem = new TreeItem<>("Actions");

        for (DTOAction action : actions) {
            TreeItem<String> actionItem = new TreeItem<>(action.getType());
            actionsItem.getChildren().add(actionItem);
        }

        ruleItem.getChildren().add(actionsItem);
    }


    @FXML
    void selectItem(MouseEvent event) {
        TreeItem<String> selectedItem = simTreeView.getSelectionModel().getSelectedItem();
        PreviewData selectedSimPreviewData;

        if(selectedItem != null){
            try {
                // Check if preview data is not null to avoid nullptr exceptions.
                if(selectedItem.isLeaf() && updatedSimulationsPreviewData != null){
                    if(selectedItem.getValue().equals("General")) {
                        selectedSimPreviewData = getSelectedSimPreviewDataByName(selectedItem.getParent().getValue());
                        setGeneralComponent(selectedItem, selectedSimPreviewData);
                    }
                    else {
                        String engineObjectType = selectedItem.getParent().getValue();
                        if(engineObjectType.equals("Environment Variables")){
                            selectedSimPreviewData = getSelectedSimPreviewDataByName(selectedItem.getParent().getParent().getValue());
                            setEnvVarsComponent(selectedItem, selectedSimPreviewData);
                        }
                        else {
                            engineObjectType = selectedItem.getParent().getParent().getValue();
                            if(engineObjectType.equals("Entities")){
                                selectedSimPreviewData = getSelectedSimPreviewDataByName(selectedItem.getParent().getParent().getParent().getValue());
                                setEntitiesComponent(selectedItem, selectedItem.getParent().getValue(), selectedSimPreviewData);
                            } else if (engineObjectType.equals("Rules")) {
                                // The item is a rule's activation
                                selectedSimPreviewData = getSelectedSimPreviewDataByName(selectedItem.getParent().getParent().getParent().getValue());
                                setRulesActivationComponent(selectedItem, selectedItem.getParent().getValue(), selectedSimPreviewData);
                            }
                            else {
                                engineObjectType = selectedItem.getParent().getParent().getParent().getValue();
                                if(engineObjectType.equals("Rules")) {
                                    // The item is a rule's actions
                                    selectedSimPreviewData = getSelectedSimPreviewDataByName(selectedItem.getParent().getParent().getParent().getParent().getValue());
                                    setRulesActionComponent(selectedItem, selectedItem.getParent().getParent().getValue(), selectedSimPreviewData);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                mainController.showMessageInNotificationBar(String.format("Error: %s", e.getMessage()));
            }
        }
    }

    private void setEnvVarsComponent(TreeItem<String> selectedItem, PreviewData previewData) throws IOException {
        EnvironmentVarDetailsController environmentVariablesComponentController = (EnvironmentVarDetailsController)displayComponentController.loadFXMLComponent("environment/EnvironmentVarDetails.fxml");
        displayComponentController.setLblTitle(selectedItem.getValue());
        for(DTOEnvironmentVariable environmentVariable : previewData.getEnvVariables()) {
            if(environmentVariable.getName().equals(selectedItem.getValue())) {
                environmentVariablesComponentController.setComponentDet(environmentVariable);
                break;
            }
        }
    }

    private void setEntitiesComponent(TreeItem<String> selectedItem, String entityName, PreviewData previewData) throws IOException {
        PropertyDetailsController propertyDetailsController = (PropertyDetailsController)displayComponentController.loadFXMLComponent("entity/property/PropertyDetails.fxml");
        displayComponentController.setLblTitle(selectedItem.getValue());
        for (DTOEntity entity : previewData.getEntities()) {
            if(entity.getName().equals(entityName)){
                for (DTOProperty property : entity.getProperties()) {
                    if(property.getName().equals(selectedItem.getValue())){
                        propertyDetailsController.setComponentDet(property);
                        break;
                    }
                }
                break;
            }
        }
    }

    private void setRulesActionComponent(TreeItem<String> selectedItem, String ruleName, PreviewData previewData) throws IOException {
        ActionDetailsController actionDetailsController = (ActionDetailsController)displayComponentController.loadFXMLComponent("rule/action/ActionDetails.fxml");
        displayComponentController.setLblTitle(selectedItem.getValue());
        for (DTORule rule : previewData.getRules()){
            if(rule.getName().equals(ruleName)) {
                for (DTOAction action : rule.getActions()) {
                    if(action.getType().equals(selectedItem.getValue())){
                        actionDetailsController.setComponentDet(action);
                        break;
                    }
                }
                break;
            }
        }
    }

    private void setRulesActivationComponent(TreeItem<String> selectedItem, String ruleName, PreviewData previewData) throws IOException {
        ActivationDetailsController activationDetailsController = (ActivationDetailsController)displayComponentController.loadFXMLComponent("rule/activation/ActivationDetails.fxml");
        displayComponentController.setLblTitle(String.format("Activation (%s)", selectedItem.getParent().getValue()));
        for(DTORule rule : previewData.getRules()) {
            if(rule.getName().equals(ruleName)){
                activationDetailsController.setComponentDet(rule);
                break;
            }
        }
    }

    private void setGeneralComponent(TreeItem<String> selectedItem, PreviewData previewData) throws IOException {
        GeneralDetailsController generalDetailsController = (GeneralDetailsController)displayComponentController.loadFXMLComponent("general/GeneralDetails.fxml");
        displayComponentController.setLblTitle(selectedItem.getValue());
        generalDetailsController.setComponentDet(previewData.getEndingConditions(), previewData.getGridAndThread());
    }

    private void clearTreeView() {
        if(!simulationsItem.isLeaf()) {
            simulationsItem.getChildren().clear();
        }
    }

    @Override
    public List<EventListener> getAllFileLoadedListeners() {
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(this);
        listeners.add(displayComponentController);

        return listeners;
    }

    @Override
    public void onFileLoaded(PreviewData previewData, boolean isFirstSimulationLoaded) {
        clearTreeView();
        displayComponentController.clearGridPaneCell();
        //this.previewData = previewData;
        //updateSimTreeView();
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }

    public void startSimBreakdownRefresher() {
        simulationBreakdownRefresher = new SimulationBreakdownRefresher(this);
        timer = new Timer();
        timer.schedule(simulationBreakdownRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    private PreviewData getSelectedSimPreviewDataByName(String simName) {
        PreviewData ret = null;

        for (PreviewData previewData : updatedSimulationsPreviewData.getPreviewDataArray()) {
            if(previewData.getSimulationName().equals(simName)) {
                ret = previewData;
                break;
            }
        }

        return ret;
    }
}
