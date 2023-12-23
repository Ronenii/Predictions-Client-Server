package gui.app.menu.management.simulation;

import gui.app.api.Controller;
import gui.app.menu.management.ManagementComponentController;
import gui.app.menu.management.simulation.breakdown.SimBreakdownMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import manager.AdminServerAgent;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.prview.SimulationsPreviewData;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SimulationManagerComponentController implements Controller {

    private ManagementComponentController mainController;

    @FXML
    private TextField textFieldPath;

    @FXML
    private Button buttonLoadFile;

    @FXML
    private ListView<String> listViewSimulations;
    private Map<String, Integer> loadedSimulationsNamesMap; // The purpose of 'loadedSimulationsNamesMap' is to hold the simulations names, the integer value is irrelevant.

    private String currentLoadedFilePath;

    public void setMainController(ManagementComponentController controller) {
        this.mainController = controller;
    }

    @FXML
    private void initialize() {
        currentLoadedFilePath = "";
        this.loadedSimulationsNamesMap = new HashMap<>();
    }

    @FXML
    void loadFileButtonActionListener(ActionEvent event) {
        File selectedFile = selectAndValidateTextFile();

        if (selectedFile != null) {
            AdminServerAgent.uploadFile(selectedFile, this);
            textFieldPath.setText(selectedFile.getPath());
        }
    }

    File selectAndValidateTextFile() {
        /// Initial FCD settings
        FileChooser fileChooser = new FileChooser();

        // Set the title of the dialog
        fileChooser.setTitle("Open settings file");

        // Set the starting dir of the dialog to be desktop
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (isXmlFile(selectedFile.getName())){
            return selectedFile;
        }
        else{
            mainController.showMessageInNotificationBar("ERROR: The selected file is not an XML file.");
            return null;
        }
    }

    boolean isXmlFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);

        return extension.equalsIgnoreCase("xml");

    }

    @FXML
    void loadFileTextFieldListener(ActionEvent event) {
        if (!textFieldPath.getText().equals(currentLoadedFilePath)) {
            File file = new File(textFieldPath.getText());
        }
    }

    public void loadSimulationsListView(SimulationsPreviewData simulationsPreviewData) {
        Arrays.stream(simulationsPreviewData.getPreviewDataArray())
                .filter(previewData -> !loadedSimulationsNamesMap.containsKey(previewData.getSimulationName()))
                .forEach(previewData -> {
                    updateSimulationsListView(previewData.getSimulationName());
                });

        mainController.enableThreadComponent();
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
    void onSimulationSelected(MouseEvent event) {
        AdminServerAgent.getSimulationPreviewData(this, listViewSimulations.getSelectionModel().getSelectedItem().toString());
    }

    public void loadSimBreakdownComponent(PreviewData previewData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("breakdown/SimBreakdownMenu.fxml"));
            Parent root = loader.load();
            Stage simBreakdownWindow = new Stage();
            simBreakdownWindow.setTitle(String.format("%s's details", previewData.getSimulationName()));
            SimBreakdownMenuController controller = (SimBreakdownMenuController)loader.getController();
            controller.updateSimTreeView(previewData);
            Image icon = new Image(getClass().getResourceAsStream("../../../../../res/icon/icon.png"));
            simBreakdownWindow.getIcons().add(icon);
            Scene scene = new Scene(root);
            simBreakdownWindow.setScene(scene);
            simBreakdownWindow.show();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addAdminLoadSimulationNames(String[] simulationNames) {
        Arrays.stream(simulationNames).forEach(this::updateSimulationsListView);
    }

    @Override
    public void showMessageInNotificationBar(String message) {
        mainController.showMessageInNotificationBar(message);
    }
}
