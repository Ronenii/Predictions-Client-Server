package gui.header.component;

import server2client.simulation.prview.PreviewData;
import gui.api.EngineCommunicator;
import gui.app.AppController;
import gui.app.mode.AppMode;
import gui.header.component.queue.manager.QueueManagerComponentController;
import gui.result.models.QueueManagementData;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import jaxb.event.FileLoadedEvent;
import manager.EngineAgent;

import java.io.File;

public class HeaderComponentController implements FileLoadedEvent, EngineCommunicator {
    @FXML
    private GridPane currentMainComponent;
    private AppController mainController;
    @FXML
    private Button loadFileBTN;

    @FXML
    private GridPane queueManagerComponent;
    @FXML private QueueManagerComponentController queueManagerComponentController;

    @FXML
    private TextField pathTF;
    @FXML
    private Label predictionLabel;
    @FXML
    private ComboBox<String> themesCB;
    @FXML
    private CheckBox bonusCheckBox;

    private Scene scene;
    private String currentLoadedFilePath;

    public void setMainController(AppController mainController) {
        setOnChecked(null);
        this.mainController = mainController;
        currentLoadedFilePath = "";
    }

    public boolean isBonusSelected(){
        return bonusCheckBox.isSelected();
    }

    @FXML
    private void setOnChecked(ActionEvent event){
        if(bonusCheckBox.isSelected())
        {
            themesCB.visibleProperty().set(true);
        }
        else{
            // Select the default theme
            themesCB.visibleProperty().set(false);
            themesCB.getSelectionModel().selectFirst();
        }
    }

    public void initialize() {
        if(queueManagerComponentController != null) {
            queueManagerComponentController.setMainController(this);
        }

        themesCB.setItems(FXCollections.observableArrayList("Default mode", "Light mode", "Dark mode"));

    }

    public void setSceneForThemes(Scene scene){
        this.scene = scene;
    }

    @FXML
    void selectedItemComboBox(ActionEvent event) {
        String selectedItem = themesCB.getSelectionModel().getSelectedItem();

        switch (selectedItem) {
            case "Default mode":
                mainController.clearMode();
                break;
            case "Light mode":
                mainController.changeToLightMode();
                break;
            case "Dark mode":
                mainController.changeToDarkMode();
                break;

        }
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
            //loadFile(selectedFile);
        }
    }

//    /**
//     * Tries to load a simulation file. If successful sets it as the header text field text
//     * And notifies the user that the load succeeded. Otherwise, notifies that the load failed
//     * and displays the errors.
//     */
//    private void loadFile(File fileToLoad){
//        try {
//            currentLoadedFilePath = fileToLoad.getPath();
//            getEngineAgent().loadSimulationFromFile(fileToLoad,mainController.getAllFileLoadedListeners());
//        }catch (IllegalArgumentException e){
//            mainController.showNotification(e.getMessage());
//        }
//    }

    @FXML
    void loadFileTextFieldListener(ActionEvent event) {
        if (!pathTF.getText().equals(currentLoadedFilePath)) {
            File file = new File(pathTF.getText());

            //loadFile(file);
        }
    }

    @FXML
    void resetTextOnMouseEnteredTextFieldListener(MouseEvent event) {
        resetTextFieldIfNotFocused();
    }

    @FXML
    void resetTextOnMouseExitTextFieldListener(MouseEvent event) {
        resetTextFieldIfNotFocused();
    }

    private void resetTextFieldIfNotFocused() {
        if (!pathTF.isFocused()) {
            pathTF.setText(currentLoadedFilePath);
        }
    }

    @Override
    public void onFileLoaded(PreviewData previewData, boolean isFirstSimulationLoaded) {
        pathTF.setText(currentLoadedFilePath);
        queueManagerComponentController.clearComponent();
        mainController.showNotification("File has been loaded successfully!");
    }

    @Override
    public EngineAgent getEngineAgent() {
        return mainController.getEngineAgent();
    }

    public void updateQueueLblInQueueManagement() {
        queueManagerComponentController.incrementLblQueue();
    }

    public void updateRunningAndCompletedLblsInQueueManagement(QueueManagementData queueManagementData) {
        queueManagerComponentController.updateRunningAndCompletedLblsInQueueManagement(queueManagementData);
    }

    public void changeToDarkMode() {
        currentMainComponent.getStylesheets().add(getClass().getResource("themes/DarkMode.css").toExternalForm());
    }

    public void changeToLightMode() {
        currentMainComponent.getStylesheets().add(getClass().getResource("themes/LightMode.css").toExternalForm());
    }

    public void clearMode(AppMode appMode) {
        switch (appMode) {
            case DARK:
                currentMainComponent.getStylesheets().remove(getClass().getResource("themes/DarkMode.css").toExternalForm());
                break;
            case LIGHT:
                currentMainComponent.getStylesheets().remove(getClass().getResource("themes/LightMode.css").toExternalForm());
                break;
        }
    }
}