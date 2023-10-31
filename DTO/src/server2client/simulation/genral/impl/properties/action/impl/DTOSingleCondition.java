package server2client.simulation.genral.impl.properties.action.impl;

public class DTOSingleCondition {
    private final int thenActionCount;
    private final int elseActionCount;
    private final String value;
    private final String operator;

    public DTOSingleCondition(int thenActionCount, int elseActionCount, String value, String operator) {
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
