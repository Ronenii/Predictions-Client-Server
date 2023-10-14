package server2client.simulation.genral.impl.properties.action.impl;

import server2client.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOSingleCondition extends DTOAction {
    private final int thenActionCount;
    private final int elseActionCount;
    private final String value;
    private final String operator;

    public DTOSingleCondition(String type, String mainEntity, String secondaryEntity, String property, int thenActionCount, int elseActionCount, String value, String operator) {
        super(type, mainEntity, secondaryEntity, property);
        this.thenActionCount = thenActionCount;
        this.elseActionCount = elseActionCount;
        this.value = value;
        this.operator = operator;
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

}
