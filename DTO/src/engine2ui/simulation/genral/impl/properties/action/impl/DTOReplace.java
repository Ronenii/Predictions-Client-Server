package engine2ui.simulation.genral.impl.properties.action.impl;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOReplace extends DTOAction {
    private final String createEntity;
    private final String replaceType;

    public DTOReplace(String name, String type, String mainEntity, String secondaryEntity, String property, String createEntity, String replaceType) {
        super(name, type, mainEntity, secondaryEntity, property);
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
