package gui.app.menu.management.simulation.breakdown.details.rule.action;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecondaryEntityTypeController {

    @FXML
    private Label lblSecondaryEntityType;

    public void setComponentDet(String lblDet){
        lblSecondaryEntityType.setText(lblDet);
    }

}
