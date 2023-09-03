package gui.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Predictions");

        Parent load = FXMLLoader.load(getClass().getResource("app/PredictionApp.fxml"));
        Scene scene = new Scene(load, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
