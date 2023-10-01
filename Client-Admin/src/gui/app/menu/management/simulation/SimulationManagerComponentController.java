package gui.app.menu.management.simulation;

import gui.app.menu.management.ManagementComponentController;
import gui.app.menu.management.simulation.data.LoadedSimulationData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class SimulationManagerComponentController {

    private ManagementComponentController mainController;

    @FXML
    private TextField textFieldPath;

    @FXML
    private Button buttonLoadFile;

    @FXML
    private ListView<LoadedSimulationData> listViewSimulations;

    private String currentLoadedFilePath;


    public void setMainController(ManagementComponentController managementComponentController) {
        this.mainController = managementComponentController;
    }

    @FXML
    private void initialize(){
        currentLoadedFilePath = "";
    }

    @FXML
    void loadFileButtonActionListener(ActionEvent event) {
        /// Initial FCD settings
        FileChooser fileChooser = new FileChooser();

        // Set the title of the dialog
        fileChooser.setTitle("Open settings file");

        // Set the starting dir of the dialog to be desktop
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));

        File selectedFile = fileChooser.showOpenDialog(null);

//        if (selectedFile != null) {
//            loadFile(selectedFile);
//        }
    }

    @FXML
    void loadFileTextFieldListener(ActionEvent event) {
        if (!textFieldPath.getText().equals(currentLoadedFilePath)) {
            File file = new File(textFieldPath.getText());

//            loadFile(file);
        }
    }

    @FXML
    void onSimulationSelected(ContextMenuEvent event) {

    }
}