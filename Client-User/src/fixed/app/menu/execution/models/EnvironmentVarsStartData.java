package fixed.app.menu.execution.models;

import engine2ui.simulation.genral.impl.properties.DTOEnvironmentVariable;

import java.util.Map;

public class EnvironmentVarsStartData {
    private final Map<String, Object> envVarsValuesMap;

    public EnvironmentVarsStartData(Map<String, Object> envVarsValuesMap) {
        this.envVarsValuesMap = envVarsValuesMap;
    }

    public Map<String, Object> getEnvVarsValuesMap() {
        return envVarsValuesMap;
    }
}
