package gui.simulation.breakdown.details.rule.activation;

import engine2ui.simulation.genral.impl.properties.DTORule;
import engine2ui.simulation.start.DTOEnvironmentVariable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ActivationDetailsController {

    @FXML
    private Label lblTIcks;

    @FXML
    private Label lblProbability;

    public void setComponentDet(DTORule rule) {
        lblTIcks.setText(String.valueOf(rule.getTicks()));
        lblProbability.setText(String.valueOf(rule.getProbability()));
    }

}
