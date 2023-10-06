package server2client.simulation.genral.impl.properties.action.impl;

import server2client.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOSet extends DTOAction {
    private final String value;

    public DTOSet(String type, String mainEntity, String secondaryEntity, String property, String value) {
        super(type, mainEntity, secondaryEntity, property);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
