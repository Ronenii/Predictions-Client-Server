package gui.result.tab.statistics;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import gui.result.tab.ResultTabComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class StatisticsComponentController {
    private ResultTabComponentController mainController;

    @FXML
    private TreeView<?> entityTreeView;

    @FXML
    private TableView<?> histogramTableView;

    @FXML
    private Label consistencyLabel;

    @FXML
    private HBox averageHbox;

    @FXML
    private Label AverageLabel;

    public void setMainController(ResultTabComponentController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void onMouseClickedTreeView(MouseEvent event) {
        
    }

    /**
     * TODO: Load the result data entities and properties into the tree view.
     * Each property in the tree view will fill out the histogram table view and the statistics about it.
     */
    public void loadComponent(DTOEntity[] entities){
        clearComponent();
    };

    public void clearComponent(){
        entityTreeView.setRoot(null);
        AverageLabel.setText("");
        consistencyLabel.setText("");
        histogramTableView.getItems().clear();
    }



}
