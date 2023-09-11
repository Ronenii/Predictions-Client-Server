package engine2ui.simulation.genral.impl.properties;

import java.util.List;

public class StartData{
    private final List<DTOEnvironmentVariable> environmentVariables;

    public StartData(List<DTOEnvironmentVariable> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public List<DTOEnvironmentVariable> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void addEnvironmentVariable(DTOEnvironmentVariable environmentVariable) {
        environmentVariables.add(environmentVariable);
    }

    public void addEnvironmentVariable(String name, String type, double from, double to) {
        environmentVariables.add(new DTOEnvironmentVariable(name, type, from, to));
    }
}
