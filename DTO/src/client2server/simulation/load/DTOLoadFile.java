package client2server.simulation.load;

import java.io.File;
import java.util.EventListener;
import java.util.List;

public class DTOLoadFile {
    private final File file;
    private final List<EventListener> listeners;

    public DTOLoadFile(File file, List<EventListener> listeners) {
        this.file = file;
        this.listeners = listeners;
    }

    public List<EventListener> getListeners() {
        return listeners;
    }

    public File getFile() {
        return file;
    }
}
