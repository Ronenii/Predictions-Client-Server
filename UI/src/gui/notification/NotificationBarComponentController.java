package gui.notification;

import gui.api.event.ErrorEvent;
import gui.app.AppController;
import gui.notification.window.NotificationWindowController;
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
import java.util.EventListener;

public class NotificationBarComponentController implements ErrorEvent {

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

    /**
     * Sets the notification bar text.
     * If the text is longer than the width of the screen or if there are multiple lines
     * in the notification, shows a hyperlink which allows the user to expand and see
     * the entire message.
     */
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

    /**
     * Removes the expand hyperLink from the screen if it exists.
     */
    private void removeExpandLabel(){
        if(!hBoxExpand.getChildren().isEmpty()){
            hBoxExpand.getChildren().remove(0);
        }
    }

    /**
     * Creates an expand hyperlink and adds it to the notification bar if no expand link exists.
     */
    private void addExpandHyperLinkToLabel(String text){
        // Create the hyperlink that creates the error window when clicked.
        Hyperlink expandLink = new Hyperlink("expand");
        expandLink.minWidth(expandLink.getWidth());
        expandLink.setOnAction(event -> showNotificationWindow(text));
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

    /**
     * Opens a window containing the entire given notification
     */
    private void showNotificationWindow(String notification) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Notification");
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("window/NotificationWindow.fxml");
            Parent root = fxmlLoader.load(url.openStream());
            NotificationWindowController controller = fxmlLoader.getController();
            controller.initialize(notification);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void onErrorEvent(String error) {
        showNotificationWindow(error);
    }
}

