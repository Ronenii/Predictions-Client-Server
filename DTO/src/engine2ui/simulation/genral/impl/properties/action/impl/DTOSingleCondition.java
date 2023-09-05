package engine2ui.simulation.genral.impl.properties.action.impl;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOSingleCondition extends DTOAction {
    private final int thenAndElseActionCount;
    private final String value;
    private final String operator;
    private final String propety;

    public DTOSingleCondition(String name, String type, String mainEntity, String secondaryEntity, String property, int thenAndElseActionCount, String value, String operator, String propety) {
        super(name, type, mainEntity, secondaryEntity, property);
        this.thenAndElseActionCount = thenAndElseActionCount;
        this.value = value;
        this.operator = operator;
        this.propety = propety;
    }

    public int getThenAndElseActionCount() {
        return thenAndElseActionCount;
    }

    public String getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }

    public String getPropety() {
        return propety;
    }
}
