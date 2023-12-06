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

    private String pattern = "^[A-Za-z0-9().\\-_,?! ]+$";

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    @FXML
    void loginButtonActionListener(ActionEvent event) {
        if(usernameTF.getText().isEmpty()) {
            setErrorMessage("Please insert a username (At least one character)");
        } else if(!usernameTF.getText().matches(pattern)) {
            setErrorMessage("Please insert a username using only the characters A-Z, a-z,\n 0-9, and the following special characters: ().-_,?!");
        } else {
            UserServerAgent.connect(usernameTF.getText(), this);
        }
    }

    @FXML
    void usernameTextFieldActionListener(ActionEvent event) {
        loginButtonActionListener(event);
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
