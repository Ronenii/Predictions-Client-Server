import gui.app.AdminAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import manager.AdminServerAgent;

import java.io.IOException;
import java.net.URL;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        setupPrimaryStage(primaryStage);
    }

    private void setupPrimaryStage(Stage primaryStage) {
        Image icon = new Image(getClass().getResourceAsStream("res/icon/icon.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Predictions-Admin");
        FXMLLoader fxmlLoader = createFXMLLoader("gui/app/AdminApp.fxml");
        Parent root = loadFXML(fxmlLoader);
        AdminAppController appController = fxmlLoader.getController();
        appController.setPrimaryStageOnClose(primaryStage);
        setupOnCloseEventHandler(primaryStage, appController);
        AdminServerAgent.connect(appController);
        showPrimaryStage(primaryStage, root);
    }

    private FXMLLoader createFXMLLoader(String fxmlPath) {
        URL url = getClass().getResource(fxmlPath);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }

    private Parent loadFXML(FXMLLoader fxmlLoader) {
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            // Handle the exception appropriately (e.g., log or display an error)
            e.printStackTrace();
            return null;
        }
    }

    private void setupOnCloseEventHandler(Stage primaryStage, AdminAppController appController) {
        primaryStage.setOnCloseRequest(event -> {
            AdminServerAgent.disconnect(appController);
            System.exit(0);
        });
    }

    private void showPrimaryStage(Stage primaryStage, Parent root) {
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("mainGUI");
        launch(args);
    }
}