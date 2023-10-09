package server2client.simulation.genral.impl.properties.action.api;

public abstract class DTOAction {
    private final String type;
    private final String mainEntity;
    private final String secondaryEntity;
    private final String property;

    public DTOAction(String type, String mainEntity, String secondaryEntity, String property) {
        this.type = type;
        this.mainEntity = mainEntity;
        this.secondaryEntity = secondaryEntity;
        this.property = property;
    }

    public String getType() {
        return type;
    }

    public String getMainEntity() {
        return mainEntity;
    }

    public String getSecondaryEntity() {
        return secondaryEntity;
    }
}
