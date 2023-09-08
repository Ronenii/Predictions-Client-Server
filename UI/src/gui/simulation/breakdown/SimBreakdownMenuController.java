package gui.simulation.breakdown;
import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTORule;
import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.prview.PreviewData;
import engine2ui.simulation.genral.impl.properties.DTOEnvironmentVariable;
import gui.api.event.handler.HasFileLoadedListeners;
import gui.simulation.breakdown.details.DisplayComponentController;
import gui.simulation.breakdown.details.entity.property.PropertyDetailsController;
import gui.simulation.breakdown.details.environment.EnvironmentVarDetailsController;
import gui.simulation.breakdown.details.general.GeneralDetailsController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import jaxb.event.FileLoadedEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.ResourceBundle;

public class SimBreakdownMenuController implements Initializable, HasFileLoadedListeners, FileLoadedEvent {

    private SubMenusController mainController;
    private PreviewData previewData;
    @FXML
    private TreeView<String> simTreeView;
    @FXML
    private ScrollPane displayComponent;
    @FXML
    private DisplayComponentController displayComponentController;
    private TreeItem<String> envVarsItem;
    private TreeItem<String> entitiesItem;
    private TreeItem<String> rulesItem;
    private TreeItem<String> generalItem;

    public void setMainController(SubMenusController mainController) {
        this.mainController = mainController;
    }

    public void setPreviewData(PreviewData previewData) {
        this.previewData = previewData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> world = new TreeItem<>("World");
        simTreeView.setRoot(world);
        envVarsItem = new TreeItem<>("Environment Variables");
        entitiesItem = new TreeItem<>("Entities");
        rulesItem = new TreeItem<>("Rules");
        generalItem = new TreeItem<>("General");
        world.getChildren().addAll(envVarsItem, entitiesItem, rulesItem, generalItem);
    }

    public void updateSimTreeView() {
        updateEnvVarsInTreeView(previewData.getEnvVariables());
        updateEntitiesInTreeView(previewData.getEntities());
        updateRulesInTreeView(previewData.getRules());
    }

    private void updateEnvVarsInTreeView(List<DTOEnvironmentVariable> envVariables) {
        TreeItem<String> envVarItem;

        for(DTOEnvironmentVariable envVar : envVariables) {
            envVarItem = new TreeItem<>(envVar.getName());
            envVarsItem.getChildren().add(envVarItem);
        }
    }

    private void updateEntitiesInTreeView(List<DTOEntity> entities) {
        TreeItem<String> entityItem;

        for (DTOEntity entity : entities) {
            entityItem = new TreeItem<>(entity.getName());
            updateEntityPropertiesInTreeView(entityItem, entity.getProperties());
            entitiesItem.getChildren().add(entityItem);
        }
    }

    private void updateEntityPropertiesInTreeView(TreeItem<String> entityItem, DTOProperty[] entityProp) {
        TreeItem<String> propertyItem;

        for (DTOProperty property : entityProp) {
            propertyItem = new TreeItem<>(property.getName());
            entityItem.getChildren().add(propertyItem);
        }
    }

    private void updateRulesInTreeView(List<DTORule> rules) {
        TreeItem<String> ruleItem;

        for (DTORule rule : rules) {
            ruleItem = new TreeItem<>(rule.getName());
            updateRuleActionsInTreeView(ruleItem, rule.getActions());
            TreeItem<String> activationItem = new TreeItem<>("Activation");
            ruleItem.getChildren().add(activationItem);
            //Todo: assign activationItem its detail component.
            rulesItem.getChildren().add(ruleItem);
        }
    }

    private void updateRuleActionsInTreeView(TreeItem<String> ruleItem, List<DTOAction> actions) {
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

        if(selectedItem.isLeaf()){
            try {
                if(selectedItem != null){
                    if(selectedItem.getValue().equals("General")) {
                        setGeneralComponent(selectedItem);
                    }
                    else {
                        String engineObjectType = selectedItem.getParent().getValue();
                        if(engineObjectType.equals("Environment Variables")){
                            setEnvVarsComponent(selectedItem);
                        }
                        else {
                            engineObjectType = selectedItem.getParent().getParent().getValue();
                            if(engineObjectType.equals("Entities")){
                                setEntitiesComponent(selectedItem, selectedItem.getParent().getValue());
                            }
                            else {
                                engineObjectType = selectedItem.getParent().getParent().getParent().getValue();
                                if(engineObjectType.equals("Rules")) {

                                }
                                else {
                                    //Todo: error occurred
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                //Todo: mashu
            }
        }
    }

    private void setEnvVarsComponent(TreeItem<String> selectedItem) throws IOException {
        EnvironmentVarDetailsController environmentVariablesComponentController = (EnvironmentVarDetailsController)displayComponentController.loadFXMLComponent("environment/EnvironmentVarDetails.fxml");
        displayComponentController.setLblTitle(selectedItem.getValue());
        for(DTOEnvironmentVariable environmentVariable : previewData.getEnvVariables()) {
            if(environmentVariable.getName().equals(selectedItem.getValue())) {
                environmentVariablesComponentController.setComponentDet(environmentVariable);
                break;
            }
        }
    }

    private void setEntitiesComponent(TreeItem<String> selectedItem, String entityName) throws IOException {
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

    private void setGeneralComponent(TreeItem<String> selectedItem) throws IOException {
        GeneralDetailsController generalDetailsController = (GeneralDetailsController)displayComponentController.loadFXMLComponent("general/GeneralDetails.fxml");
        displayComponentController.setLblTitle(selectedItem.getValue());
        generalDetailsController.setComponentDet(previewData.getEndingConditions(), previewData.getGridAndThread());

    }

    @Override
    public List<EventListener> getAllFileLoadedListeners() {
        //TODO: Get all file loaded listeners to from sub components
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(this);
        listeners.add(displayComponentController);

        return listeners;
    }

    @Override
    public void onFileLoaded(PreviewData previewData) {
        this.previewData = previewData;
        updateSimTreeView();
    }
}
