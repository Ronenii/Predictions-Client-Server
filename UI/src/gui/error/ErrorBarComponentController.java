package gui.error;

import gui.app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorBarComponentController {

    private AppController mainController;
    @FXML
    private Label lblError;

    @FXML
    private HBox hBoxMessageBox;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    //TODO: Add the option to open a new window when a text prompt is clicked
    public void setLblErrorText(String text) {
        String[] lines = text.split("\n");

        if (lines.length == 1) {
            lblError.setText(text);
        } else {
            lblError.setText(lines[0]);

        }
    }

    private void displayBarMessage(String firstLine) {
        lblError.setText(firstLine);

        Text textNode = new Text(firstLine + " expand");

        if (textNode.getLayoutBounds().getWidth() > lblError.getWidth()) {
            Hyperlink expandLink = new Hyperlink("expand");
            lblError.setGraphic(new Text("\n"));
            lblError.setGraphicTextGap(5);
            expandLink.setOnAction(event -> showErrorWindow(firstLine));
            hBoxMessageBox.getChildren().add(expandLink);
        }
    }

    private void showErrorWindow(String errorMessage) {
        Stage stage = new Stage();
        stage.setTitle("Errors");


    }


}

