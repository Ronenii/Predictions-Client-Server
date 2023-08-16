package engine2ui.simulation.start;

import engine2ui.simulation.genral.api.HasList;

import java.util.List;

public class StartData implements HasList {
    private final List<DTOEnvironmentVariable> environmentVariables;

    public StartData(List<DTOEnvironmentVariable> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public void addEnvironmentVariable(DTOEnvironmentVariable environmentVariable) {
        environmentVariables.add(environmentVariable);
    }

    public void addEnvironmentVariable(String name, String type, double from, double to) {
        environmentVariables.add(new DTOEnvironmentVariable(name, type, from, to));
    }
}
