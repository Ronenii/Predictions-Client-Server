package gui.simulation.breakdown.details;

import engine2ui.simulation.prview.PreviewData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import jaxb.event.FileLoadedEvent;

import java.io.IOException;
import java.net.URL;

public class DisplayComponentController implements FileLoadedEvent {

    @FXML
    private GridPane grdDisplay;

    @FXML
    private Label lblTitle;


    public void setLblTitle(String name) {
        lblTitle.setText(name);
    }

    public Object loadFXMLComponent(String fxmlFileName) throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        FXMLLoader loader = new FXMLLoader(DisplayComponentController.class.getResource(fxmlFileName));
        clearGridPaneCell();
        grdDisplay.add(loader.load(),1, 3);

        return loader.getController();
    }

    private void clearGridPaneCell() {
        for (Node node : grdDisplay.getChildren()) {
            if (GridPane.getRowIndex(node) == 3 && GridPane.getColumnIndex(node) == 1) {
                grdDisplay.getChildren().remove(node);
                break;
            }
        }
    }

    @Override
    public void onFileLoaded(PreviewData previewData) {
        lblTitle.setText("The simulation creation has completed successfully");
    }
}