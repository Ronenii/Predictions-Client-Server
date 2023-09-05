package gui.simulation.breakdown;
import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTOEndingCondition;
import engine2ui.simulation.genral.impl.properties.DTOGridAndThread;
import engine2ui.simulation.genral.impl.properties.DTORule;
import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.prview.PreviewData;
import engine2ui.simulation.start.DTOEnvironmentVariable;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SimBreakdownMenuController implements Initializable {

    private SubMenusController mainController;
    @FXML
    private TreeView<String> simTreeView;
    private TreeItem<String> envVarsItem;
    private TreeItem<String> entitiesItem;
    private TreeItem<String> rulesItem;
    private TreeItem<String> generalItem;

    public void setMainController(SubMenusController mainController) {
        this.mainController = mainController;
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

    public void updateSimTreeView(PreviewData previewData) {
        updateEnvVarsInTreeView(previewData.getEnvVariables());
        updateEntitiesInTreeView(previewData.getEntities());
        updateRulesInTreeView(previewData.getRules());
        updateGeneralInTreeView(previewData.getEndingConditions(), previewData.getGridAndThread());
    }

    private void updateEnvVarsInTreeView(List<DTOEnvironmentVariable> envVariables) {
        TreeItem<String> envVarItem;

        for(DTOEnvironmentVariable envVar : envVariables) {
            envVarItem = new TreeItem<>(envVar.getName());
            envVarsItem.getChildren().add(envVarItem);
            // Todo: assign envVarItem its detail component.
        }
    }

    private void updateEntitiesInTreeView(List<DTOEntity> entities) {
        TreeItem<String> entityItem;

        for (DTOEntity entity : entities) {
            entityItem = new TreeItem<>(entity.getName());
            updateEntityPropertiesInTreeView(entityItem, entity.getProperties());
            envVarsItem.getChildren().add(entityItem);
        }
    }

    private void updateEntityPropertiesInTreeView(TreeItem<String> entityItem, DTOProperty[] entityProp) {
        TreeItem<String> propertyItem;

        for (DTOProperty property : entityProp) {
            propertyItem = new TreeItem<>(property.getName());
            entityItem.getChildren().add(propertyItem);
            // Todo: assign propertyItem its detail component.
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

    private void updateRuleActionsInTreeView(TreeItem<String> ruleItem, DTOAction[] actions) {
        TreeItem<String> actionItem;

        for (DTOAction action : actions) {
            actionItem = new TreeItem<>(action.getName());
            ruleItem.getChildren().add(actionItem);
            // Todo: assign actionItem its detail component.
        }
    }

    private void updateGeneralInTreeView(List<DTOEndingCondition> endingConditions, DTOGridAndThread gridAndThread){

    }




    @FXML
    void selectItem(MouseEvent event) {

    }
}
