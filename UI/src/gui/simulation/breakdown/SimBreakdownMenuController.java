package gui.simulation.breakdown;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SimBreakdownMenuController implements Initializable {

    @FXML
    private TreeView<String> simTreeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> world = new TreeItem<>("World");
        simTreeView.setRoot(world);
        TreeItem<String> envVar = new TreeItem<>("Environment Variables");
        TreeItem<String> entities = new TreeItem<>("Entities");
        TreeItem<String> rules = new TreeItem<>("Rules");
        TreeItem<String> general = new TreeItem<>("General");
        world.getChildren().addAll(envVar, entities, rules, general);


    }

    @FXML
    void selectItem(MouseEvent event) {

    }
}
