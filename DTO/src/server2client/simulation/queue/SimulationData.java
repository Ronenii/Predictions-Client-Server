package server2client.simulation.queue;

public class SimulationData {
    private final String requestedBy;
    private final String simId;
    private final String status;

    public SimulationData(String requestedBy, String simId, String status) {
        this.requestedBy = requestedBy;
        this.simId = simId;
        this.status = status;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public String getSimId() {
        return simId;
    }

    public String getStatus() {
        return status;
    }
}
