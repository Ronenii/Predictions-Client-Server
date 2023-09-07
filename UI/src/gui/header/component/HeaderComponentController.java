package gui.header.component;

import gui.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class HeaderComponentController {
    private AppController mainController;
    @FXML
    private Button loadFileBTN;
    @FXML
    private Button queueManBTN;
    @FXML
    private TextField pathTF;
    @FXML
    private Label predictionLabel;

    private String currentLoadedFilePath;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
        currentLoadedFilePath = "";
    }

    /**
     * Opens a file choose dialog (FCD) and tries to load the file given.
     */
    @FXML
    void loadFileButtonActionListener(ActionEvent event) {
        /// Initial FCD settings
        FileChooser fileChooser = new FileChooser();

        // Set the title of the dialog
        fileChooser.setTitle("Open settings file");

        // Set the starting dir of the dialog to be desktop
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null){
            loadFileAndUpdateHeader(selectedFile);
        }
    }

    private void loadFileAndUpdateHeader(File fileToLoad){
        try {
            mainController.engineAgent.loadSimulationFromFile(fileToLoad);
            pathTF.setText(fileToLoad.getPath());
            currentLoadedFilePath = fileToLoad.getPath();
            mainController.showNotification("File has been loaded successfully!");
        }catch (IllegalArgumentException e){
            mainController.showNotification(e.getMessage());
        }
    }

    @FXML
    void loadFileTextFieldListener(ActionEvent event) {
        if(!pathTF.getText().equals(currentLoadedFilePath)){
            File file = new File(pathTF.getText());

            loadFileAndUpdateHeader(file);
        }
    }

    @FXML
    void queueManButtonActionListener(ActionEvent event) {

    }


    @FXML
    void resetTextOnMouseEnteredTextFieldListener(MouseEvent event) {
        resetTextFieldIfNotFocused();
    }

    @FXML
    void resetTextOnMouseExitTextFieldListener(MouseEvent event) {
        resetTextFieldIfNotFocused();
    }

    private void resetTextFieldIfNotFocused(){
        if(!pathTF.isFocused()){
            pathTF.setText(currentLoadedFilePath);
        }
    }

}