package server2client.simulation.request;

import server2client.simulation.request.data.DTOSingleRequest;

public class DTORequests {
    private DTOSingleRequest[] requests;

    public DTORequests(DTOSingleRequest[] requests) {
        this.requests = requests;
    }

    public DTOSingleRequest[] getRequests() {
        return requests;
    }
}
