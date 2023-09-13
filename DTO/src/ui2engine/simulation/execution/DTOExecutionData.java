package ui2engine.simulation.execution;

import ui2engine.simulation.execution.user.input.EnvPropertyUserInput;

import java.util.HashMap;
import java.util.Map;

public class DTOExecutionData {
    private final Map<String, EnvPropertyUserInput> envPropertyUserInputs;

    private final Map<String, Integer> entityPopulationUserInputs;

    public DTOExecutionData() {
        envPropertyUserInputs = new HashMap<>();
        entityPopulationUserInputs = new HashMap<>();
    }

    public Map<String,EnvPropertyUserInput> getEnvPropertyUserInputs() {
        return envPropertyUserInputs;
    }

    public Map<String, Integer> getEntityPopulationUserInputs() {
        return entityPopulationUserInputs;
    }

    public void updateEnvPropertyUserInputs(String name, boolean isRandomInit, Object value) {
        envPropertyUserInputs.put(name, new EnvPropertyUserInput(name, isRandomInit, value));
    }

    public void updateEntityPopulationUserInputs(String name, int population) {
        entityPopulationUserInputs.put(name, population);
    }
}
