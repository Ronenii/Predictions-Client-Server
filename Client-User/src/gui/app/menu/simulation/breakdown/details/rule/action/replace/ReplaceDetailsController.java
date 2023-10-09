package gui.app.menu.simulation.breakdown.details.rule.action.replace;

import server2client.simulation.genral.impl.properties.action.impl.DTOReplace;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReplaceDetailsController {

    @FXML
    private Label lblReplaceEntity;

    @FXML
    private Label lblReplaceType;

    public void setComponentDet(DTOReplace replace) {
        lblReplaceEntity.setText(replace.getCreateEntity());
        lblReplaceType.setText(replace.getReplaceType());
    }
}
