package server2client.simulation.genral.impl.properties.action.impl;

public class DTOProximity {
    private final String targetEntity;
    private final String depth;
    private final int subActionsCount;

    public DTOProximity(String targetEntity, String depth, int subActionsCount) {
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
