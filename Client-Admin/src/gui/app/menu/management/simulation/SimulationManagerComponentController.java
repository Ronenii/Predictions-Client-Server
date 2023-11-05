package gui.app.menu.management.simulation;

import gui.app.api.Controller;
import gui.app.menu.MenuComponentController;
import gui.app.menu.management.ManagementComponentController;
import gui.app.menu.management.simulation.data.LoadedSimulationData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;
import manager.AdminServerAgent;
import server2client.simulation.prview.SimulationsPreviewData;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SimulationManagerComponentController implements Controller {

    private Controller mainController;

    @FXML
    private TextField textFieldPath;

    @FXML
    private Button buttonLoadFile;

    @FXML
    private ListView<String> listViewSimulations;
    private Map<String,Integer> loadedSimulationsNamesMap; // The purpose of 'loadedSimulationsNamesMap' is to hold the simulations names, the integer value is irrelevant.

    private String currentLoadedFilePath;

    public void setMainController(ManagementComponentController controller){
        this.mainController = controller;
    }

    @FXML
    private void initialize() {
        currentLoadedFilePath = "";
        this.loadedSimulationsNamesMap = new HashMap<>();
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

        if (selectedFile != null) {
            AdminServerAgent.uploadFile(selectedFile, this);
            textFieldPath.setText(selectedFile.getPath());
        }
    }

    @FXML
    void loadFileTextFieldListener(ActionEvent event) {
        if (!textFieldPath.getText().equals(currentLoadedFilePath)) {
            File file = new File(textFieldPath.getText());

//            loadFile(file);
        }
    }

    public void loadSimulationsListView(SimulationsPreviewData simulationsPreviewData) {
        Arrays.stream(simulationsPreviewData.getPreviewDataArray())
                .filter(previewData -> !loadedSimulationsNamesMap.containsKey(previewData.getSimulationName()))
                .forEach(previewData -> {
                    updateSimulationsListView(previewData.getSimulationName());
                });
    }

    private void updateSimulationsListView(String simName) {
        listViewSimulations.getItems().add(simName);
        // The purpose of 'loadedSimulationsNamesMap' is to hold the simulations names, the integer value is irrelevant.
        loadedSimulationsNamesMap.put(simName, 0);
    }

    public String getSelectedSimulationName() {
        return listViewSimulations.getSelectionModel().getSelectedItem();
    }

    @FXML
    void onSimulationSelected(ContextMenuEvent event) {
        //Todo: this method update the simulation queue manager.
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
