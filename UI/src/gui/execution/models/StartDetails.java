package gui.execution.models;

import java.util.HashMap;
import java.util.Map;

public class StartDetails {
    private final EntitiesStartData entitiesStartData;
    private final EnvironmentVarsStartData environmentVarsStartData;

    public StartDetails(EntitiesStartData entitiesStartData, EnvironmentVarsStartData environmentVarsStartData) {
        this.entitiesStartData = entitiesStartData;
        this.environmentVarsStartData = environmentVarsStartData;
    }

    public EntitiesStartData getEntitiesStartData() {
        return entitiesStartData;
    }

    public EnvironmentVarsStartData getEnvironmentVarsStartData() {
        return environmentVarsStartData;
    }
}
