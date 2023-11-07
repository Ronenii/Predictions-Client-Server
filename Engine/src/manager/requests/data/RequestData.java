package manager.requests.data;

import simulation.properties.ending.conditions.EndingCondition;

import java.util.List;

public class RequestData {
    public int requestId;
    private final String username;
    private final String simulationName;
    private final int tokens;
    private final List<EndingCondition> endingConditions;
    private RequestStatus status;

    public RequestData(String username, String simulationName, int tokens, List<EndingCondition> endingConditions) {
        this.username = username;
        this.simulationName = simulationName;
        this.tokens = tokens;
        this.endingConditions = endingConditions;
        this.status = RequestStatus.PENDING;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getUsername() {
        return username;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public int getTokens() {
        return tokens;
    }

    public List<EndingCondition> getEndingConditions() {
        return endingConditions;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
