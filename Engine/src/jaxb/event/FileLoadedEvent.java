package jaxb.event;

import java.util.EventListener;

/**
 * An event for when a file has been properly loaded in the system.
 */
public interface FileLoadedEvent extends EventListener {
    void onFileLoaded();
}
