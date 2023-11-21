package server2client.simulation.request.data;

import server2client.simulation.genral.impl.properties.DTOEndingCondition;

public class DTOSingleRequest {
    private final int reqId;
    private final String simName;
    private final String username;
    private final int tokens;
    private final DTOEndingCondition[] endingConditions;

    public DTOSingleRequest(int reqId, String simName, String username, int tokens, DTOEndingCondition[] endingConditions) {
        this.reqId = reqId;
        this.simName = simName;
        this.username = username;
        this.tokens = tokens;
        this.endingConditions = endingConditions;
    }

    public int getReqId() {
        return reqId;
    }

    public String getSimName() {
        return simName;
    }

    public String getUsername() {
        return username;
    }

    public int getTokens() {
        return tokens;
    }

    public DTOEndingCondition[] getEndingConditions() {
        return endingConditions;
    }
}
