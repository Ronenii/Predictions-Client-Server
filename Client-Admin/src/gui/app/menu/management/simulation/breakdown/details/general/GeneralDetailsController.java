package gui.app.menu.management.simulation.breakdown.details.general;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import server2client.simulation.genral.impl.properties.DTOGrid;

public class GeneralDetailsController {

    @FXML
    private Label lblGridHeight;

    @FXML
    private Label lblGridWidth;


    public void setComponentDet(DTOGrid gridAndThread) {
        lblGridHeight.setText(String.valueOf(gridAndThread.getGridRows()));
        lblGridWidth.setText(String.valueOf(gridAndThread.getGridColumns()));
    }

}
