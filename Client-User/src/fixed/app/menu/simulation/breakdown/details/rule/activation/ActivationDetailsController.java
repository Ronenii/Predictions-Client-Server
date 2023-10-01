package fixed.app.menu.simulation.breakdown.details.rule.activation;

import engine2ui.simulation.genral.impl.properties.DTORule;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ActivationDetailsController {

    @FXML
    private Label lblTicks;

    @FXML
    private Label lblProbability;

    public void setComponentDet(DTORule rule) {
        lblTicks.setText(String.valueOf(rule.getTicks()));
        lblProbability.setText(String.valueOf(rule.getProbability()));
    }

}
