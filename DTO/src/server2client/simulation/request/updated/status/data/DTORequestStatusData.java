package server2client.simulation.request.updated.status.data;

public class DTORequestStatusData {
    private final int reqId;
    private final String reqStatus;

    public DTORequestStatusData(int reqId, String reqStatus) {
        this.reqId = reqId;
        this.reqStatus = reqStatus;
    }

    public int getReqId() {
        return reqId;
    }

    public String getReqStatus() {
        return reqStatus;
    }
}
