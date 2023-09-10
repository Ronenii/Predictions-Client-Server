package gui.simulation.breakdown.details.rule.action.increase.decrease;

import engine2ui.simulation.genral.impl.properties.action.impl.DTOIncreaseOrDecrease;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class IncreaseOrDecreaseDetailsController {

    @FXML
    private Label lblValue;

    public void setComponentDet(DTOIncreaseOrDecrease increaseOrDecrease) {
        lblValue.setText(increaseOrDecrease.getValue());
    }
}
