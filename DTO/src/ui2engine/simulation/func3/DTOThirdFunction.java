package ui2engine.simulation.func3;

import ui2engine.simulation.func3.user.input.EnvPropertyUserInput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DTOThirdFunction {
    private Set<EnvPropertyUserInput> envPropertyUserInputs;

    public DTOThirdFunction() {
        envPropertyUserInputs = new HashSet<>();
    }

    public Set<EnvPropertyUserInput> getEnvPropertyUserInputs() {
        return envPropertyUserInputs;
    }

    public void updateEnvPropertyUserInputs(String name, boolean isRandomInit, Object value){
        envPropertyUserInputs.add(new EnvPropertyUserInput(name, isRandomInit, value));
    }
}
