package server2client.simulation.admin.load.details;

import server2client.simulation.request.DTORequests;

public class AdminLoadDetails {
    private final String[] simulationsNames;
    private final DTORequests unansweredRequests;
    private final String[] loadedSimulationsId;

    public AdminLoadDetails(String[] simulationsNames, DTORequests unansweredRequests, String[] loadedSimulationsId) {
        this.simulationsNames = simulationsNames;
        this.unansweredRequests = unansweredRequests;
        this.loadedSimulationsId = loadedSimulationsId;
    }

    public String[] getSimulationsNames() {
        return simulationsNames;
    }

    public DTORequests getUnansweredRequests() {
        return unansweredRequests;
    }

    public String[] getLoadedSimulationsId() {
        return loadedSimulationsId;
    }
}
