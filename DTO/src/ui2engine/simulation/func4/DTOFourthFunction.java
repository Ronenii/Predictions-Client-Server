package ui2engine.simulation.func4;

import java.util.UUID;

public class DTOFourthFunction {
    private UUID uniqeId;
    private PastSimulationShowType type;

    public DTOFourthFunction(UUID uniqeId, PastSimulationShowType type) {
        this.uniqeId = uniqeId;
        this.type = type;
    }

    public UUID getUniqeId() {
        return uniqeId;
    }

    public PastSimulationShowType getType() {
        return type;
    }
}
