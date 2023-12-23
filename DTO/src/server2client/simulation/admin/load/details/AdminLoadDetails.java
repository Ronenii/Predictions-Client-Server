package server2client.simulation.admin.load.details;

import server2client.simulation.queue.NewSimulationsData;
import server2client.simulation.request.DTORequests;

public class AdminLoadDetails {
    private final String[] simulationsNames;
    private final DTORequests unansweredRequests;
    private final NewSimulationsData loadedSimulations;

    public AdminLoadDetails(String[] simulationsNames, DTORequests unansweredRequests, NewSimulationsData loadedSimulations) {
        this.simulationsNames = simulationsNames;
        this.unansweredRequests = unansweredRequests;
        this.loadedSimulations = loadedSimulations;
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
}
