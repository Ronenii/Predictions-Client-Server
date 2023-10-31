package gui.app.menu.simulation.breakdown.details.entity.property;

import server2client.simulation.genral.impl.properties.DTOProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PropertyDetailsController {

    @FXML
    private Label lblType;

    @FXML
    private Label lblRandInit;

    @FXML
    private Label lblRange;

    public void setComponentDet(DTOProperty property) {
        lblType.setText(property.getType());
        if(property.isHasRange()){
            String text = String.format("From %.2f to %.2f", property.getFrom(), property.getTo());
            lblRange.setText(text);
        }
        else {
            lblRange.setText("-");
        }

        if(property.isRandomInit()){
            lblRandInit.setText("Yes");
        }
        else {
            lblRandInit.setText("No");
        }
    }
}
