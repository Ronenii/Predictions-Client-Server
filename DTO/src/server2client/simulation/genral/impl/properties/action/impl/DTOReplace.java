package server2client.simulation.genral.impl.properties.action.impl;

public class DTOReplace {
    private final String createEntity;
    private final String replaceType;

    public DTOReplace(String createEntity, String replaceType) {
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
