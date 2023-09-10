package gui.simulation.breakdown.details.rule.action;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;
import engine2ui.simulation.genral.impl.properties.action.impl.*;
import gui.simulation.breakdown.details.rule.action.calculation.CalculationDetailsController;
import gui.simulation.breakdown.details.rule.action.condition.MultipleConditionDetailsController;
import gui.simulation.breakdown.details.rule.action.condition.SingleConditionDetailsController;
import gui.simulation.breakdown.details.rule.action.increase.decrease.IncreaseOrDecreaseDetailsController;
import gui.simulation.breakdown.details.rule.action.proximity.ProximityDetailsController;
import gui.simulation.breakdown.details.rule.action.replace.ReplaceDetailsController;
import gui.simulation.breakdown.details.rule.action.set.SetDetailsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.awt.*;
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
        lblPrimeEntity.setText(action.getMainEntity());
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
        if (action instanceof DTOIncreaseOrDecrease){
            DTOIncreaseOrDecrease dtoIncreaseOrDecrease = (DTOIncreaseOrDecrease)action;
            IncreaseOrDecreaseDetailsController increaseOrDecreaseDetailsController = (IncreaseOrDecreaseDetailsController)loadFXMLComponent("increase.decrease/IncreaseOrDecreaseDetails.fxml");
            increaseOrDecreaseDetailsController.setComponentDet(dtoIncreaseOrDecrease);
        } else if (action instanceof DTOCalculation) {
            DTOCalculation dtoCalculation = (DTOCalculation)action;
            CalculationDetailsController  calculationDetailsController = (CalculationDetailsController)loadFXMLComponent("calculation/CalculationDetails.fxml");
            calculationDetailsController.setComponentDet(dtoCalculation);
        } else if (action instanceof DTOSingleCondition) {
            DTOSingleCondition dtoSingleCondition = (DTOSingleCondition)action;
            SingleConditionDetailsController singleConditionDetailsController = (SingleConditionDetailsController)loadFXMLComponent("condition/SingleConditionDetails.fxml");
            singleConditionDetailsController.setComponentDet(dtoSingleCondition);
        } else if (action instanceof DTOMultipleCondition) {
            DTOMultipleCondition dtoMultipleCondition = (DTOMultipleCondition)action;
            MultipleConditionDetailsController multipleConditionDetailsController = (MultipleConditionDetailsController)loadFXMLComponent("condition/MultipleConditionDetails.fxml");
            multipleConditionDetailsController.setComponentDet(dtoMultipleCondition);
        } else if (action instanceof DTOSet) {
            DTOSet dtoSet = (DTOSet)action;
            SetDetailsController setDetailsController = (SetDetailsController)loadFXMLComponent("set/SetDetails.fxml");
            setDetailsController.setComponentDet(dtoSet);
        } else if (action instanceof DTOReplace) {
            DTOReplace dtoReplace = (DTOReplace)action;
            ReplaceDetailsController replaceDetailsController = (ReplaceDetailsController)loadFXMLComponent("replace/ReplaceDetails.fxml");
            replaceDetailsController.setComponentDet(dtoReplace);
        } else if (action instanceof DTOProximity) {
            DTOProximity dtoProximity = (DTOProximity)action;
            ProximityDetailsController proximityDetailsController = (ProximityDetailsController)loadFXMLComponent("proximity/ProximityDetails.fxml");
            proximityDetailsController.setComponentDet(dtoProximity);
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
        int spaceIndex = type.indexOf(" ");
        return type.substring(0,spaceIndex);
    }
}
