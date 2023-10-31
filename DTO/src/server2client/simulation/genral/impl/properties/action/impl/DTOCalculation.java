package server2client.simulation.genral.impl.properties.action.impl;

public class DTOCalculation {
    private final String arg1;
    private final String arg2;
    private final String calculationType;

    public DTOCalculation(String arg1, String arg2, String calculationType) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.calculationType = calculationType;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getCalculationType() {
        return calculationType;
    }
}
