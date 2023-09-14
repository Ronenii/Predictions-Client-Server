package gui.result.tab.statistics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class StatisticsComponentController {

    @FXML
    private TreeView<?> entityTreeView;

    @FXML
    private TableView<?> histogramTableView;

    @FXML
    private Label ConsistencyLabel;

    @FXML
    private HBox averageHbox;

    @FXML
    private Label AverageLabel;

    @FXML
    void onMouseClickedTreeView(MouseEvent event) {

    }

}
