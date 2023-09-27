package gui.app.menu.managment.simulation;

import gui.app.menu.managment.ManagementComponentController;
import gui.app.menu.managment.simulation.data.LoadedSimulationData;
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
    private TextField pathTF;

    @FXML
    private Button loadFileBTN;

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
        if (!pathTF.getText().equals(currentLoadedFilePath)) {
            File file = new File(pathTF.getText());

//            loadFile(file);
        }
    }

    @FXML
    void onSimulationSelected(ContextMenuEvent event) {

    }
}
