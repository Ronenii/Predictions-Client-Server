package engine2ui.simulation.genral.impl.properties.action.impl;

import engine2ui.simulation.genral.impl.properties.action.api.DTOAction;

public class DTOProximity extends DTOAction {
    private final String targetEntity;
    private final String depth;
    private final int subActionsCount;

    public DTOProximity(String name, String type, String mainEntity, String secondaryEntity, String property, String targetEntity, String depth, int subActionsCount) {
        super(name, type, mainEntity, secondaryEntity, property);
        this.targetEntity = targetEntity;
        this.depth = depth;
        this.subActionsCount = subActionsCount;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public String getDepth() {
        return depth;
    }

    public int getSubActionsCount() {
        return subActionsCount;
    }
}
