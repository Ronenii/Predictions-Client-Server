package engine2ui.simulation.genral.impl.properties.action.api;

public abstract class DTOAction {
    private final String name;
    private final String type;
    private final String mainEntity;
    private final String secondaryEntity;
    private final String property;

    public DTOAction(String name, String type, String mainEntity, String secondaryEntity, String property) {
        this.name = name;
        this.type = type;
        this.mainEntity = mainEntity;
        this.secondaryEntity = secondaryEntity;
        this.property = property;
    }

    public String getName() {
        return name;
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
