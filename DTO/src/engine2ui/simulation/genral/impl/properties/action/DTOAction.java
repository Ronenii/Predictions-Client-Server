package engine2ui.simulation.genral.impl.properties.action;

public abstract class DTOAction {
    private final String name;
    private final String type;
    private final String mainEntity;
    private final String secondaryEntity;

    public DTOAction(String name, String type, String mainEntity, String secondaryEntity) {
        this.name = name;
        this.type = type;
        this.mainEntity = mainEntity;
        this.secondaryEntity = secondaryEntity;
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
