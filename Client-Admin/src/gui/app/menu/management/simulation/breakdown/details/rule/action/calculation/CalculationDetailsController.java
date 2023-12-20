package gui.app.menu.management.simulation.breakdown.details.rule.action.calculation;

import server2client.simulation.genral.impl.properties.action.impl.DTOCalculation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CalculationDetailsController {

    @FXML
    private Label lblCalcType;

    @FXML
    private Label lblArg1;

    @FXML
    private Label lblArg2;

    public void setComponentDet(DTOCalculation calculation){
        lblCalcType.setText(calculation.getCalculationType());
        lblArg1.setText(calculation.getArg1());
        lblArg2.setText(calculation.getArg2());
    }

}
