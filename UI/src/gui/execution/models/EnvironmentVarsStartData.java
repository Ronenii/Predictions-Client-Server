package gui.execution.models;

import engine2ui.simulation.genral.impl.properties.DTOEnvironmentVariable;

import java.util.Map;

public class EnvironmentVarsStartData {
    private final Map<DTOEnvironmentVariable, String> environmentVariableMap;

    public EnvironmentVarsStartData(Map<DTOEnvironmentVariable, String> environmentVariableMap) {
        this.environmentVariableMap = environmentVariableMap;
    }

    public Map<DTOEnvironmentVariable, String> getEnvironmentVariableMap() {
        return environmentVariableMap;
    }
}
