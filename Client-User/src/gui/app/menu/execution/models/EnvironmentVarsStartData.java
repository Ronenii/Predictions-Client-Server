package gui.app.menu.execution.models;

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
