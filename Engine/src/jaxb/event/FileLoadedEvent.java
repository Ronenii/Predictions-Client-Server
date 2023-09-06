package jaxb.event;

import java.util.EventListener;

public interface FileLoadedEvent extends EventListener {
    void onFileLoaded();
}
