package server2client.simulation.request;

public class DTORequests {
    private DTOSingleRequest[] requests;

    public DTORequests(DTOSingleRequest[] requests) {
        this.requests = requests;
    }

    public DTOSingleRequest[] getRequests() {
        return requests;
    }
}
