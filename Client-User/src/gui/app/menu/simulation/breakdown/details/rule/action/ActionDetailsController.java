package gui.app.menu.simulation.breakdown.details.rule.action;

import server2client.simulation.genral.impl.properties.action.api.DTOAction;
import gui.app.menu.simulation.breakdown.details.rule.action.calculation.CalculationDetailsController;
import gui.app.menu.simulation.breakdown.details.rule.action.condition.MultipleConditionDetailsController;
import gui.app.menu.simulation.breakdown.details.rule.action.condition.SingleConditionDetailsController;
import gui.app.menu.simulation.breakdown.details.rule.action.increase.decrease.IncreaseOrDecreaseDetailsController;
import gui.app.menu.simulation.breakdown.details.rule.action.proximity.ProximityDetailsController;
import gui.app.menu.simulation.breakdown.details.rule.action.replace.ReplaceDetailsController;
import gui.app.menu.simulation.breakdown.details.rule.action.set.SetDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import server2client.simulation.genral.impl.properties.action.api.DTOActionType;
import server2client.simulation.genral.impl.properties.action.impl.*;

import java.io.IOException;

public class ActionDetailsController {

    @FXML
    private Label lblType;

    @FXML
    private Label lblPrimeEntity;

    @FXML
    private Label lblHasSecondEntity;

    @FXML
    private VBox actionVB;

    @FXML
    private VBox vboxExtras;


    public void setComponentDet(DTOAction action) throws IOException {
        lblType.setText(extractActionType(action.getType()));
        if(action.getActionType() == DTOActionType.MULTIPLE){
            lblPrimeEntity.setText("-");
        }
        else {
            lblPrimeEntity.setText(action.getMainEntity());
        }

        if(action.getSecondaryEntity() != null){
            lblHasSecondEntity.setText("Yes");
            loadSecondaryEntityComponent(action);
        }
        else {
            lblHasSecondEntity.setText("No");
        }

        loadActionTypeComponent(action);
    }

    private void loadActionTypeComponent(DTOAction action) throws IOException {
        // DTOKill is not handled because this action does not contain any additional information besides what is held by the abstract DTOAction class.
        switch (action.getActionType()){
            case INCREASE_OR_DECREASE:
                IncreaseOrDecreaseDetailsController increaseOrDecreaseDetailsController = (IncreaseOrDecreaseDetailsController)loadFXMLComponent("increase.decrease/IncreaseOrDecreaseDetails.fxml");
                increaseOrDecreaseDetailsController.setComponentDet(action);
                break;
            case CALCULATION:
                CalculationDetailsController calculationDetailsController = (CalculationDetailsController)loadFXMLComponent("calculation/CalculationDetails.fxml");
                calculationDetailsController.setComponentDet(action.getDtoCalculation());
                break;
            case SINGLE:
                SingleConditionDetailsController singleConditionDetailsController = (SingleConditionDetailsController)loadFXMLComponent("condition/SingleConditionDetails.fxml");
                singleConditionDetailsController.setComponentDet(action.getDtoSingleCondition());
                break;
            case MULTIPLE:
                MultipleConditionDetailsController multipleConditionDetailsController = (MultipleConditionDetailsController)loadFXMLComponent("condition/MultipleConditionDetails.fxml");
                multipleConditionDetailsController.setComponentDet(action.getDtoMultipleCondition());
                break;
            case SET:
                SetDetailsController setDetailsController = (SetDetailsController)loadFXMLComponent("set/SetDetails.fxml");
                setDetailsController.setComponentDet(action);
                break;
            case REPLACE:
                ReplaceDetailsController replaceDetailsController = (ReplaceDetailsController)loadFXMLComponent("replace/ReplaceDetails.fxml");
                replaceDetailsController.setComponentDet(action.getDtoReplace());
                break;
            case PROXIMITY:
                ProximityDetailsController proximityDetailsController = (ProximityDetailsController)loadFXMLComponent("proximity/ProximityDetails.fxml");
                proximityDetailsController.setComponentDet(action.getDtoProximity());
                break;
        }
    }

    private Object loadFXMLComponent(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));

        actionVB.getChildren().add(loader.load());
        return loader.getController();
    }

    private void loadSecondaryEntityComponent(DTOAction action) throws IOException {
        SecondaryEntityTypeController secondaryEntityTypeController = (SecondaryEntityTypeController)loadFXMLComponent("SecondaryEntityType.fxml");

        secondaryEntityTypeController.setComponentDet(action.getSecondaryEntity());
    }

    private String extractActionType(String type) {
        int hashTagIndex = type.indexOf("#");
        return type.substring(0,hashTagIndex - 1);
    }
}
