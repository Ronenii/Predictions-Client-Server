package gui.app.notification;

import gui.app.AdminAppController;
import gui.app.api.Controller;
import gui.app.notification.window.LogWindowController;
import gui.app.notification.window.LogWindowController;
import javafx.animation.FillTransition;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationBarComponentController implements Controller {

    private Controller mainController;
    @FXML
    private Label lblNotification;

    @FXML
    private HBox hBoxExpand;

    @FXML
    private GridPane grdParent;

    private StringBuilder logs;

    Stage logWindow;

    @FXML
    private LogWindowController logWindowController;

    @Override
    public void setMainController(Controller controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        logs = new StringBuilder();
        grdParent.visibleProperty().set(false);
    }

    @FXML
    private Circle notificationCircle;

    private void playNotificationAnimation() {
            FillTransition fillTransition = new FillTransition(Duration.millis(700), notificationCircle);
            fillTransition.setFromValue(Color.WHITE);
            fillTransition.setToValue(Color.web("#19fc11"));
            fillTransition.setCycleCount(4);
            fillTransition.play();
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
    private void addExpandHyperLinkToLabel(String text) {
        // Create the hyperlink that creates the error window when clicked.
        Hyperlink expandLink = new Hyperlink("expand");
        expandLink.getStyleClass().add("expand-link");
        expandLink.minWidth(expandLink.getWidth());
        expandLink.setOnAction(event -> showLogWindow(logs.toString()));
        VBox vBox = new VBox(expandLink);
        VBox.setMargin(expandLink, new Insets(5, 0, 0, 0));
        // Set a bit of space between the label and the hyperlink
        lblNotification.setGraphic(new Text("\n"));
        lblNotification.setGraphicTextGap(5);

        if (hBoxExpand.getChildren().isEmpty()) {
            hBoxExpand.getChildren().add(vBox);
            hBoxExpand.minWidth(hBoxExpand.getWidth());
        }
    }

    /**
     * Opens a window containing the entire given notification
     */
    private void showLogWindow(String logs) {
        try {
            logWindow = new Stage();
            logWindow.setTitle("Program logs");
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("window/LogWindow.fxml");
            Parent root = fxmlLoader.load(url.openStream());
            logWindowController = (LogWindowController)fxmlLoader.getController();
            logWindowController.initialize(logs);
            Scene scene = new Scene(root);
            logWindow.setScene(scene);
            logWindow.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void turnOnNotificationBar() {
        grdParent.visibleProperty().set(true);
    }

    public void changeToDarkMode() {
        grdParent.getStylesheets().add(getClass().getResource("themes/DarkMode.css").toExternalForm());
    }

    public void changeToLightMode() {
        grdParent.getStylesheets().add(getClass().getResource("themes/LightMode.css").toExternalForm());
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        playNotificationAnimation();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        addExpandHyperLinkToLabel(message);
        LocalDateTime ldt = LocalDateTime.now();
        String toDisplay = ldt.format(dtf) + "- " + message + "\n\n";
        logs.insert(0, toDisplay);
        setLblNotificationText(toDisplay);

        if (logWindow != null && logWindowController != null) {
            logWindowController.changeTextAreaText(logs.toString());
        }
    }

//    public void clearMode(AppMode appMode) {
//        switch (appMode) {
//            case DARK:
//                grdParent.getStylesheets().remove(getClass().getResource("themes/DarkMode.css").toExternalForm());
//                break;
//            case LIGHT:
//                grdParent.getStylesheets().remove(getClass().getResource("themes/LightMode.css").toExternalForm());
//                break;
//        }
//    }

}

