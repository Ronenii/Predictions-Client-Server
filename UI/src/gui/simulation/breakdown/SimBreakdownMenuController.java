package gui.simulation.breakdown;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SimBreakdownMenuController implements Initializable {

    private SubMenusController mainController;
    @FXML
    private TreeView<String> simTreeView;

    public void setMainController(SubMenusController mainController) {
        this.mainController = mainController;
    }

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
