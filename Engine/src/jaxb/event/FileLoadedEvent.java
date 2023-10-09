package jaxb.event;

import server2client.simulation.prview.PreviewData;

import java.util.EventListener;

/**
 * An event for when a file has been properly loaded in the system.
 */
public interface FileLoadedEvent extends EventListener {
    void onFileLoaded(PreviewData previewData, boolean isFirstSimulationLoaded);
}
