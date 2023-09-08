package ui2engine.simulation.func3;

import ui2engine.simulation.func3.user.input.EntityPopulationUserInput;
import ui2engine.simulation.func3.user.input.EnvPropertyUserInput;

import java.util.HashMap;
import java.util.Map;

public class DTOExecutionData {
    private Map<String, EnvPropertyUserInput> envPropertyUserInputs;

    private Map<String, Integer> entityPopulationUserInputs;

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
