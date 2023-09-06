package gui.error;

import gui.app.AppController;
import gui.error.popup.PopupErrorWindowController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class ErrorBarComponentController {

    private AppController mainController;
    @FXML
    private Label lblError;

    @FXML
    private HBox hBoxMessageBox;

    @FXML
    private HBox hBoxExpand;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
    }

    //TODO: Add the option to open a new window when a text prompt is clicked
    public void setLblErrorText(String errMessage) {
        String[] lines = errMessage.split("\n");

        // Creates an image of the given text, this is done to compare the width of the text with the width of the label.
        Text textNode = new Text(lines[0] + " expand");
        textNode.snapshot(null,null);
        lblError.setText(lines[0]);

        if ((textNode.getLayoutBounds().getWidth() > lblError.getWidth()) || lines.length > 1) {
            addExpandHyperLinkToLabel(errMessage);
        }
    }

    private void addExpandHyperLinkToLabel(String text){
        // Create the hyperlink that creates the error window when clicked.
        Hyperlink expandLink = new Hyperlink("expand");
        expandLink.minWidth(expandLink.getWidth());
        expandLink.setOnAction(event -> showErrorWindow(text));

        // Set a bit of space between the label and the hyperlink
        lblError.setGraphic(new Text("\n"));
        lblError.setGraphicTextGap(5);

        hBoxExpand.getChildren().add(expandLink);
        hBoxExpand.minWidth(hBoxExpand.getWidth());
    }

    private void showErrorWindow(String errorMessage) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Errors");
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("popup/PopupErrorWindow.fxml");
            Parent root = fxmlLoader.load(url.openStream());
            PopupErrorWindowController controller = fxmlLoader.getController();
            controller.initialize(errorMessage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}

