import gui.app.AdminAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Predictions");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("gui/app/PredictionApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        Scene scene = new Scene(root);
        AdminAppController appController = fxmlLoader.getController();
        appController.setPrimaryStageOnClose(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("mainGUI");
        launch(args);
    }
}