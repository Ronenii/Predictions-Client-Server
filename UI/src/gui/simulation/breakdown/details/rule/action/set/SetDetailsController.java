package gui.simulation.breakdown.details.rule.action.set;

import engine2ui.simulation.genral.impl.properties.action.impl.DTOMultipleCondition;
import engine2ui.simulation.genral.impl.properties.action.impl.DTOSet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetDetailsController {

    @FXML
    private Label lblValue;

    public void setComponentDet(DTOSet set){
        lblValue.setText(set.getValue());
    }

}
