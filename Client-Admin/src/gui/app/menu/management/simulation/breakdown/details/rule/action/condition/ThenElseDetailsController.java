package gui.app.menu.management.simulation.breakdown.details.rule.action.condition;

import server2client.simulation.genral.impl.properties.action.impl.DTOMultipleCondition;
import server2client.simulation.genral.impl.properties.action.impl.DTOSingleCondition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ThenElseDetailsController {

    @FXML
    private Label lblThenCount;

    @FXML
    private Label lblElseCount;

    public void updateThenAndElse(DTOMultipleCondition multiple, DTOSingleCondition single){
        if(single != null){
            lblThenCount.setText(String.valueOf(single.getThenActionCount()));
            lblElseCount.setText(String.valueOf(single.getElseActionCount()));
        }
        else {
            lblThenCount.setText(String.valueOf(multiple.getThenActionCount()));
            lblElseCount.setText(String.valueOf(multiple.getElseActionCount()));
        }
    }
}