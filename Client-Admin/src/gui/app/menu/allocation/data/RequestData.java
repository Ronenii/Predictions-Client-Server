package gui.app.menu.allocation.data;

import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.request.DTOSingleRequest;

public class RequestData {
    private final int requestId;
    private final String simulationName;
    private final String username;
    private final int tokens;
    private final String endingConditionsFormatted;

    public RequestData(DTOSingleRequest dtoSingleRequest) {
       requestId = dtoSingleRequest.getReqId();
       simulationName = dtoSingleRequest.getSimName();
       username = dtoSingleRequest.getUsername();
       tokens = dtoSingleRequest.getTokens();
       endingConditionsFormatted = getFormattedEndingConditionsString(dtoSingleRequest.getEndingConditions());
    }

    private String getFormattedEndingConditionsString(DTOEndingCondition[] endingConditions) {
        StringBuilder stringBuilder = new StringBuilder();
        int endingConditionsCount = endingConditions.length, counter = 1;

        for (DTOEndingCondition endingCondition : endingConditions) {
            if(endingCondition.getType().equals("USER")) {
                stringBuilder.append(endingCondition.getType());
            }else {
                stringBuilder.append(endingCondition.getType()).append(" - ").append(endingCondition.getCount());
            }

            if(counter < endingConditionsCount) {
                stringBuilder.append(", ");
            }

            counter++;
        }

        return stringBuilder.toString();
    }

    public int getRequestId() {
        return requestId;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getUsername() {
        return username;
    }

    public int getTokens() {
        return tokens;
    }

    public String getEndingConditionsFormatted() {
        return endingConditionsFormatted;
    }
}
