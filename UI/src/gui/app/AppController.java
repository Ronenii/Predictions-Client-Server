package gui.app;

import gui.api.HasFileLoadedListeners;
import gui.header.component.HeaderComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AppController implements HasFileLoadedListeners {
    @FXML private GridPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;
    @FXML private GridPane subMenus;
    @FXML private SubMenusController subMenusController;

    public EngineAgent engineAgent;

    @FXML
    public void initialize() {
        if(headerComponentController != null && subMenusController != null) {
            headerComponentController.setMainController(this);
            subMenusController.setMainController(this);
            engineAgent = new EngineAgent();
        }
    }

    public List<EventListener> getAllFileLoadedListeners(){
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(headerComponentController);
        listeners.addAll(subMenusController.getAllFileLoadedListeners());
        return listeners;
    }


}
