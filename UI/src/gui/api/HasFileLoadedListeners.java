package gui.api;
import java.util.EventListener;
import java.util.List;

public interface HasFileLoadedListeners {
    List<EventListener> getAllFileLoadedListeners();
}
