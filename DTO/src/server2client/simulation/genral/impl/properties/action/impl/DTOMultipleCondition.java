package server2client.simulation.genral.impl.properties.action.impl;

public class DTOMultipleCondition {
    private final int thenActionCount;
    private final int elseActionCount;
    private final String logic;
    private final int subConditionsCount;

    public DTOMultipleCondition(int thenActionsCount, int elseActionCount, String logic, int subConditionsCount) {
        this.thenActionCount = thenActionsCount;
        this.elseActionCount = elseActionCount;
        this.logic = logic;
        this.subConditionsCount = subConditionsCount;
    }

    public int getThenActionCount() {
        return thenActionCount;
    }

    public int getElseActionCount() {
        return elseActionCount;
    }

    public String getLogic() {
        return logic;
    }

    public int getSubConditionsCount() {
        return subConditionsCount;
    }
}
