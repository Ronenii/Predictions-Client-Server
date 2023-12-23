package server2client.simulation.admin.load.details;

import server2client.simulation.queue.NewSimulationsData;
import server2client.simulation.request.DTORequests;

public class AdminLoadDetails {
    private final String[] simulationsNames;
    private final DTORequests unansweredRequests;
    private final NewSimulationsData loadedSimulations;
    private final boolean isThreadPoolInitialized;

    public AdminLoadDetails(String[] simulationsNames, DTORequests unansweredRequests, NewSimulationsData loadedSimulations, boolean isThreadPoolInitialized) {
        this.simulationsNames = simulationsNames;
        this.unansweredRequests = unansweredRequests;
        this.loadedSimulations = loadedSimulations;
        this.isThreadPoolInitialized = isThreadPoolInitialized;
    }

    public String[] getSimulationsNames() {
        return simulationsNames;
    }

    public DTORequests getUnansweredRequests() {
        return unansweredRequests;
    }

    public NewSimulationsData getLoadedSimulations() {
        return loadedSimulations;
    }

    public boolean isThreadPoolInitialized() {
        return isThreadPoolInitialized;
    }
}
