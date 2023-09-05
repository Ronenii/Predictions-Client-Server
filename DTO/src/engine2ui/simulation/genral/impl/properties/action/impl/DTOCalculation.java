package engine2ui.simulation.genral.impl.properties.action.impl;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOCalculation extends DTOAction {
    private final String arg1;
    private final String arg2;
    private final String calculationType;

    public DTOCalculation(String type, String mainEntity, String secondaryEntity, String property, String arg1, String arg2, String calculationType) {
        super(type, mainEntity, secondaryEntity, property);
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
