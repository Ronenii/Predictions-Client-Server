package gui.simulation.breakdown.details.rule.action.condition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SingleConditionDetailsController {

    @FXML
    private Label lblOperator;

    @FXML
    private Label lblValue;

    @FXML
    private Label lblProperty;
    @FXML
    private GridPane thenElseDetails;
    @FXML
    private ThenElseDetailsController thenElseDetailsController;

}
