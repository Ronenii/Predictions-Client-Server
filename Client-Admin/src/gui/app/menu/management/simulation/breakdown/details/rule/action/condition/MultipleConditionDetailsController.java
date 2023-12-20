package gui.app.menu.management.simulation.breakdown.details.rule.action.condition;

import server2client.simulation.genral.impl.properties.action.impl.DTOMultipleCondition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MultipleConditionDetailsController {

    @FXML
    private Label lblLogic;

    @FXML
    private Label lblSubConditionCount;
    @FXML
    private GridPane thenElseDetails;
    @FXML
    private ThenElseDetailsController thenElseDetailsController;

    public void setComponentDet(DTOMultipleCondition multipleCondition){
        lblLogic.setText(multipleCondition.getLogic());
        lblSubConditionCount.setText(String.valueOf(multipleCondition.getSubConditionsCount()));
        thenElseDetailsController.updateThenAndElse(multipleCondition, null);
    }

}
