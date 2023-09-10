package gui.simulation.breakdown.details.rule.action.condition;

import engine2ui.simulation.genral.impl.properties.action.impl.DTOMultipleCondition;
import engine2ui.simulation.genral.impl.properties.action.impl.DTOSingleCondition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SingleConditionDetailsController {

    @FXML
    private Label lblOperator;

    @FXML
    private Label lblValue;

    @FXML
    private Label lblProperty;
    @FXML
    private GridPane thenElseDetails;
    @FXML
    private ThenElseDetailsController thenElseDetailsController;

    public void setComponentDet(DTOSingleCondition singleCondition){
        lblOperator.setText(singleCondition.getOperator());
        lblValue.setText(singleCondition.getValue());
        lblProperty.setText(singleCondition.getPropety());
        thenElseDetailsController.updateThenAndElse(singleCondition);
    }

}
