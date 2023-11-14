package server2client.simulation.request;

import server2client.simulation.genral.impl.properties.DTOEndingCondition;

public class DTOSingleRequest {
    private final String simName;
    private final String username;
    private final int tokens;
    private final DTOEndingCondition[] endingConditions;

    public DTOSingleRequest(String simName, String username, int tokens, DTOEndingCondition[] endingConditions) {
        this.simName = simName;
        this.username = username;
        this.tokens = tokens;
        this.endingConditions = endingConditions;
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
