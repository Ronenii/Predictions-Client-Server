package gui.app.menu.management.simulation.breakdown.details.rule.action.condition;

import server2client.simulation.genral.impl.properties.action.impl.DTOSingleCondition;
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
       // lblProperty.setText(singleCondition.getProperty());
        thenElseDetailsController.updateThenAndElse(null, singleCondition);
    }

}
