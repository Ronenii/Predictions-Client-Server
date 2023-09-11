package engine2ui.simulation.genral.impl.properties.action.impl;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOSingleCondition extends DTOAction {
    private final int thenActionCount;
    private final int elseActionCount;
    private final String value;
    private final String operator;
    private final String property;

    public DTOSingleCondition(String type, String mainEntity, String secondaryEntity, String property, int thenActionCount, int elseActionCount, String value, String operator, String propety) {
        super(type, mainEntity, secondaryEntity, property);
        this.thenActionCount = thenActionCount;
        this.elseActionCount = elseActionCount;
        this.value = value;
        this.operator = operator;
        this.property = propety;
    }

    public int getThenActionCount() {
        return thenActionCount;
    }

    public int getElseActionCount() {
        return elseActionCount;
    }

    public String getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }

    public String getProperty() {
        return property;
    }
}
