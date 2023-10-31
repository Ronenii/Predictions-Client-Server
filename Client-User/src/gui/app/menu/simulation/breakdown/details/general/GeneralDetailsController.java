package gui.app.menu.simulation.breakdown.details.general;

import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.genral.impl.properties.DTOGridAndThread;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class GeneralDetailsController {

    @FXML
    private Label lblEndingTicks;

    @FXML
    private Label lblEndingSeconds;

    @FXML
    private Label lblGridHeight;

    @FXML
    private Label lblGridWidth;

    @FXML
    private Label lblThreadCount;

    public void setComponentDet(DTOEndingCondition[] endingConditions, DTOGridAndThread gridAndThread) {
        for (DTOEndingCondition endingCondition : endingConditions){
            if(endingCondition.getType().equals("ticks")){
                lblEndingTicks.setText(String.valueOf(endingCondition.getCount()));
            }
            else {
                lblEndingSeconds.setText(String.valueOf(endingCondition.getCount()));
            }
        }

        lblGridHeight.setText(String.valueOf(gridAndThread.getGridRows()));
        lblGridWidth.setText(String.valueOf(gridAndThread.getGridColumns()));
        lblThreadCount.setText(String.valueOf(gridAndThread.getThreadCount()));
    }

}
