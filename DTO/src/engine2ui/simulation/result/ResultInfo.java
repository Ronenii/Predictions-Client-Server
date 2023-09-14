package engine2ui.simulation.result;


import engine2ui.simulation.genral.impl.properties.DTOEndingCondition;

/**
 * TODO: This is our old DTO. Not sure what usages it has other than the console stuff we did in task 1.
 * Holds the simulation run id and the ending condition that terminate the simulation run to be sent to the user.
 */
public class ResultInfo {
    private final String id;

    private final DTOEndingCondition endingCondition;

    public ResultInfo(String id, DTOEndingCondition endingCondition) {
        this.id = id;
        this.endingCondition = endingCondition;
    }

    public String getId() {
        return id;
    }

    public DTOEndingCondition getEndingCondition() {
        return endingCondition;
    }
}
