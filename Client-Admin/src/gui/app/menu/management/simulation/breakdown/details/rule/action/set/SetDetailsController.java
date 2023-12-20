package gui.app.menu.management.simulation.breakdown.details.rule.action.set;

import server2client.simulation.genral.impl.properties.action.api.DTOAction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetDetailsController {

    @FXML
    private Label lblValue;

    public void setComponentDet(DTOAction set){
        lblValue.setText(set.getValue());
    }

}
