package gui.notification;

import gui.app.AppController;
import gui.notification.error.ErrorWindowController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class NotificationBarComponentController {

    private AppController mainController;
    @FXML
    private Label lblNotification;

    @FXML
    private HBox hBoxExpand;

    @FXML
    private GridPane grdParent;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
    }

    //TODO: Add the option to open a new window when a text prompt is clicked
    public void setLblNotificationText(String errMessage) {
        String[] lines = errMessage.split("\n");

        // Creates an image of the given text, this is done to compare the width of the text with the width of the label.
        Text textNode = new Text(lines[0] + " expand");
        textNode.snapshot(null,null);
        lblNotification.setText(lines[0]);

        if ((textNode.getLayoutBounds().getWidth() - 70 > grdParent.getWidth()) || lines.length > 1) {
            addExpandHyperLinkToLabel(errMessage);
        }
        else{
            removeExpandLabel();
        }
    }

    private void removeExpandLabel(){
        if(!hBoxExpand.getChildren().isEmpty()){
            hBoxExpand.getChildren().remove(0);
        }
    }

    private void addExpandHyperLinkToLabel(String text){
        // Create the hyperlink that creates the error window when clicked.
        Hyperlink expandLink = new Hyperlink("expand");
        expandLink.minWidth(expandLink.getWidth());
        expandLink.setOnAction(event -> showErrorWindow(text));
        VBox vBox = new VBox(expandLink);
        VBox.setMargin(expandLink, new Insets(5, 0, 0, 0));
        // Set a bit of space between the label and the hyperlink
        lblNotification.setGraphic(new Text("\n"));
        lblNotification.setGraphicTextGap(5);

        if(hBoxExpand.getChildren().isEmpty()){
            hBoxExpand.getChildren().add(vBox);
            hBoxExpand.minWidth(hBoxExpand.getWidth());
        }
    }

    private void showErrorWindow(String errorMessage) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Errors");
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("error/ErrorWindow.fxml");
            Parent root = fxmlLoader.load(url.openStream());
            ErrorWindowController controller = fxmlLoader.getController();
            controller.initialize(errorMessage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}

