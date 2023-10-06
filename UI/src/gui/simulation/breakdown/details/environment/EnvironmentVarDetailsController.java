package gui.simulation.breakdown.details.environment;

import server2client.simulation.genral.impl.properties.DTOEnvironmentVariable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EnvironmentVarDetailsController {

    @FXML
    private Label lblType;

    @FXML
    private Label lblRange;

    public void setComponentDet(DTOEnvironmentVariable environmentVariable) {
        lblType.setText(environmentVariable.getType());
        if(environmentVariable.getFrom() != null && environmentVariable.getTo() != null){
            String text = String.format("From %.2f to %.2f", environmentVariable.getFrom(), environmentVariable.getTo());
            lblRange.setText(text);
        }
        else {
            lblRange.setText("-");
        }
    }
}
