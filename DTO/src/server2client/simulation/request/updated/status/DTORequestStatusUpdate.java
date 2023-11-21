package server2client.simulation.request.updated.status;


import server2client.simulation.request.updated.status.data.DTORequestStatusData;

/**
 * This DTO object pass to the specific client(by username) an array of request their status has been changed.
 */
public class DTORequestStatusUpdate {
    private final DTORequestStatusData[] requestStatusUpdates;

    public DTORequestStatusUpdate(DTORequestStatusData[] requestStatusUpdates) {
        this.requestStatusUpdates = requestStatusUpdates;
    }

    public DTORequestStatusData[] getRequestStatusUpdates() {
        return requestStatusUpdates;
    }
}
