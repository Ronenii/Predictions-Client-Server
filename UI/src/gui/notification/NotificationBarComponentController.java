package gui.notification;

import gui.app.AppController;
import gui.notification.window.LogWindowController;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationBarComponentController {

    private AppController mainController;
    @FXML
    private Label lblNotification;

    @FXML
    private HBox hBoxExpand;

    @FXML
    private GridPane grdParent;

    private StringBuilder logs;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        logs = new StringBuilder();
    }



    public void addNotification(String notification){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        addExpandHyperLinkToLabel(notification);
        LocalDateTime ldt = LocalDateTime.now();
        String toDisplay = ldt.format(dtf) + "- " + notification + "\n\n";
        logs.append(toDisplay);
        setLblNotificationText(toDisplay);
    }

    /**
     * Sets the notification bar text.
     * If the text is longer than the width of the screen or if there are multiple lines
     * in the notification, shows a hyperlink which allows the user to expand and see
     * the entire message.
     */
    private void setLblNotificationText(String notification) {
        String[] lines = notification.split("\n");

        lblNotification.setText(lines[0]);
    }

    /**
     * Creates an expand hyperlink and adds it to the notification bar if no expand link exists.
     */
    private void addExpandHyperLinkToLabel(String text){
        // Create the hyperlink that creates the error window when clicked.
        Hyperlink expandLink = new Hyperlink("expand");
        expandLink.minWidth(expandLink.getWidth());
        expandLink.setOnAction(event -> showLogWindow(logs.toString()));
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
    private void showLogWindow(String logs) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Program logs");
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("window/LogWindow.fxml");
            Parent root = fxmlLoader.load(url.openStream());
            LogWindowController controller = fxmlLoader.getController();
            controller.initialize(logs);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

