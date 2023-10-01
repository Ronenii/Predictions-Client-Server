package fixed.app.menu.simulation.breakdown.details;

import engine2ui.simulation.prview.PreviewData;
import gui.app.mode.AppMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import jaxb.event.FileLoadedEvent;
import java.io.IOException;

public class DisplayComponentController implements FileLoadedEvent {

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

    @Override
    public void onFileLoaded(PreviewData previewData, boolean isFirstSimulationLoaded) {
        lblTitle.setText("The simulation creation has completed successfully");
    }

    public void changeToDarkMode() {
        grdDisplay.getStylesheets().add(getClass().getResource("themes/DarkMode.css").toExternalForm());
    }

    public void changeToLightMode() {
        grdDisplay.getStylesheets().add(getClass().getResource("themes/LightMode.css").toExternalForm());
    }

    public void clearMode(AppMode appMode) {
        switch (appMode) {
            case DARK:
                grdDisplay.getStylesheets().remove(getClass().getResource("themes/DarkMode.css").toExternalForm());
                break;
            case LIGHT:
                grdDisplay.getStylesheets().remove(getClass().getResource("themes/LightMode.css").toExternalForm());
                break;
        }
    }
}