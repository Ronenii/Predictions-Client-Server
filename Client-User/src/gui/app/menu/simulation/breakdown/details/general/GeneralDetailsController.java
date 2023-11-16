package gui.app.menu.simulation.breakdown.details.general;

import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.genral.impl.properties.DTOGridAndThread;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class GeneralDetailsController {

    @FXML
    private Label lblGridHeight;

    @FXML
    private Label lblGridWidth;


    public void setComponentDet(DTOGridAndThread gridAndThread) {
        lblGridHeight.setText(String.valueOf(gridAndThread.getGridRows()));
        lblGridWidth.setText(String.valueOf(gridAndThread.getGridColumns()));
    }

}
