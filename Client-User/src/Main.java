import gui.app.UserAppController;
import gui.app.login.LoginComponentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginComponentController loginComponentController = setUpLoginPage();

        if(loginComponentController.isLoggedIn()){
            Image icon = new Image(getClass().getResourceAsStream("res/icon/icon.png"));
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("Predictions-User");
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("gui/app/UserApp.fxml");
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(url.openStream());
            Scene scene = new Scene(root);
            UserAppController appController = fxmlLoader.getController();
            appController.setUsername(loginComponentController.getUsername());
            setupOnCloseEventHandler(primaryStage);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Return true if the user logged in successfully, otherwise, return false.
     */
    private LoginComponentController setUpLoginPage() throws IOException {
        Stage loginStage = new Stage();
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("gui/app/login/LoginComponent.fxml"));
        Parent loginRoot = loginLoader.load();
        LoginComponentController loginComponentController = loginLoader.getController();
        Scene loginScene = new Scene(loginRoot);
        Image icon = new Image(getClass().getResourceAsStream("res/icon/icon.png"));
        loginStage.getIcons().add(icon);

        loginStage.setTitle("Predictions - Login");
        loginStage.setResizable(false);
        loginStage.setScene(loginScene);
        loginComponentController.setLoginStage(loginStage);

        // Show the login page
        loginStage.showAndWait();

        return loginComponentController;
    }

    private void setupOnCloseEventHandler(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("mainGUI");
        launch(args);
    }
}