package gui.api;
import java.util.EventListener;
import java.util.List;

/**
 * An interface designed for Classes that contain an object that implements the FileLoadedEvent
 */
public interface HasFileLoadedListeners {
    /**
     * @return All instances of classes that implement FileLoadedEvent.
     */
    List<EventListener> getAllFileLoadedListeners();
}
