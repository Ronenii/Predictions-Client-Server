package gui.simulation.breakdown.details.rule.action.proximity;

import server2client.simulation.genral.impl.properties.action.impl.DTOProximity;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProximityDetailsController {

    @FXML
    private Label lblSrcEntity;

    @FXML
    private Label lblEnvDepth;

    @FXML
    private Label lblNumOfActions;

    public void setComponentDet(DTOProximity proximity) {
        lblSrcEntity.setText(proximity.getTargetEntity());
        lblEnvDepth.setText(proximity.getDepth());
        lblNumOfActions.setText(String.valueOf(proximity.getSubActionsCount()));
    }
}
