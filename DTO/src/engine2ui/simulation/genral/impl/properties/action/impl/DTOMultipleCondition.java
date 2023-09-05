package engine2ui.simulation.genral.impl.properties.action.impl;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOMultipleCondition extends DTOAction {
    private final int thenActionsCount;
    private final int elseActionCount;
    private final String logic;

    public DTOMultipleCondition(String type, String mainEntity, String secondaryEntity, String property, int thenActionsCount, int elseActionCount, String logic) {
        super(type, mainEntity, secondaryEntity, property);
        this.thenActionsCount = thenActionsCount;
        this.elseActionCount = elseActionCount;
        this.logic = logic;
    }

    public int getThenAndElseActionCount() {
        return thenAndElseActionCount;
    }

    public int getThenActionsCount() {
        return thenActionsCount;
    }

    public int getElseActionCount() {
        return elseActionCount;
    }

    public String getLogic() {
        return logic;
    }
}
