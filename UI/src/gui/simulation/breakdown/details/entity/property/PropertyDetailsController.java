package gui.simulation.breakdown.details.entity.property;

import server2client.simulation.genral.impl.properties.property.api.DTOProperty;
import server2client.simulation.genral.impl.properties.property.impl.RangedDTOProperty;
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
        if(property.getClass() == RangedDTOProperty.class){
            RangedDTOProperty rangedDTOProperty = (RangedDTOProperty)property;
            String text = String.format("From %.2f to %.2f", rangedDTOProperty.getFrom(), rangedDTOProperty.getTo());
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
