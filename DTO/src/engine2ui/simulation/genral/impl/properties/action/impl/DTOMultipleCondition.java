package engine2ui.simulation.genral.impl.properties.action.impl;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOMultipleCondition extends DTOAction {
    private final int thenAndElseActionCount;
    private final int subActionsCount;
    private final String logic;

    public DTOMultipleCondition(String name, String type, String mainEntity, String secondaryEntity, String property, int thenAndElseActionCount, int subActionsCount, String logic) {
        super(name, type, mainEntity, secondaryEntity, property);
        this.thenAndElseActionCount = thenAndElseActionCount;
        this.subActionsCount = subActionsCount;
        this.logic = logic;
    }

    public int getThenAndElseActionCount() {
        return thenAndElseActionCount;
    }

    public int getSubActionsCount() {
        return subActionsCount;
    }

    public String getLogic() {
        return logic;
    }
}
