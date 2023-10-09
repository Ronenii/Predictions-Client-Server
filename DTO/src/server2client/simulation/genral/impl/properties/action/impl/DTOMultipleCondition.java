package server2client.simulation.genral.impl.properties.action.impl;

import server2client.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOMultipleCondition extends DTOAction {
    private final int thenActionCount;
    private final int elseActionCount;
    private final String logic;
    private final int subConditionsCount;

    public DTOMultipleCondition(String type, String mainEntity, String secondaryEntity, String property, int thenActionsCount, int elseActionCount, String logic, int subConditionsCount) {
        super(type, mainEntity, secondaryEntity, property);
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
