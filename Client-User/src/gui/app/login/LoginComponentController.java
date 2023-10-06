package gui.app.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import manager.UserServerAgent;

public class LoginComponentController {

    Stage loginStage;
    @FXML
    private TextField usernameTF;

    @FXML
    private Button loginBTN;

    @FXML
    private Label errorMessageLabel;

    private boolean isLoggedIn;

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    @FXML
    void loginButtonActionListener(ActionEvent event) {
        if(usernameTF.getText().isEmpty()) {
            setErrorMessage("Please insert a username (At least one character)");
        } else {
            //TODO: send request to the server
            UserServerAgent.connect(usernameTF.getText(), this);
        }
    }

    public void setErrorMessage(String errorMessage) {
        errorMessageLabel.setVisible(true);
        errorMessageLabel.setText(errorMessage);
    }

    public void setLoggedIn() {
        isLoggedIn = true;
        loginStage.close();
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getUsername() {
        return usernameTF.getText();
    }
}
