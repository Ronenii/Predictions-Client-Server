package server2client.simulation.genral.impl.properties.action.impl;

import server2client.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOReplace extends DTOAction {
    private final String createEntity;
    private final String replaceType;

    public DTOReplace(String type, String mainEntity, String secondaryEntity, String property, String createEntity, String replaceType) {
        super(type, mainEntity, secondaryEntity, property);
        this.createEntity = createEntity;
        this.replaceType = replaceType;
    }

    public String getCreateEntity() {
        return createEntity;
    }

    public String getReplaceType() {
        return replaceType;
    }
}
