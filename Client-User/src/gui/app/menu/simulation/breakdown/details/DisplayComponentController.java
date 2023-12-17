package gui.app.menu.simulation.breakdown.details;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.io.IOException;

public class DisplayComponentController {

    @FXML
    private GridPane grdDisplay;

    @FXML
    private Label lblTitle;


    public void setLblTitle(String name) {
        lblTitle.setText(name);
    }

    public Object loadFXMLComponent(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        clearGridPaneCell();
        grdDisplay.add(loader.load(),1, 3);

        return loader.getController();
    }

    public void clearGridPaneCell() {
        for (Node node : grdDisplay.getChildren()) {
            if (GridPane.getRowIndex(node) == 3 && GridPane.getColumnIndex(node) == 1) {
                grdDisplay.getChildren().remove(node);
                break;
            }
        }
    }
}