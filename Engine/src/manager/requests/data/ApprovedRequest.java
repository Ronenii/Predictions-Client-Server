package manager.requests.data;

import simulation.objects.world.SimulationInstance;

public class ApprovedRequest {
    private final int reqId;
    private final String username;
    private final int tokens;
    private final SimulationInstance definitionInstance;

    public ApprovedRequest(int reqId, String username, int tokens, SimulationInstance definitionInstance) {
        this.reqId = reqId;
        this.username = username;
        this.tokens = tokens;
        this.definitionInstance = definitionInstance;
    }

    public int getReqId() {
        return reqId;
    }

    public String getUsername() {
        return username;
    }

    public int getTokens() {
        return tokens;
    }

    public SimulationInstance getDefinitionInstance() {
        return definitionInstance;
    }
}
