package engine2ui.simulation.genral.impl.properties.action.impl;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOSet extends DTOAction {
    private final String value;

    public DTOSet(String name, String type, String mainEntity, String secondaryEntity, String property, String value) {
        super(name, type, mainEntity, secondaryEntity, property);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
