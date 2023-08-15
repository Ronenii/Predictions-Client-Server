package ui2engine.simulation.func3;

import ui2engine.simulation.func3.user.input.EnvPropertyUserInput;

import java.util.HashMap;
import java.util.Map;

public class DTOThirdFunction {
    private Map<String, EnvPropertyUserInput> envPropertyUserInputs;

    public DTOThirdFunction() {
        envPropertyUserInputs = new HashMap<>();
    }

    public void updateEnvPropertyUserInputs(String name, boolean isRandomInit, Object value){
        EnvPropertyUserInput itemToAdd = new EnvPropertyUserInput(name,isRandomInit,value);
        envPropertyUserInputs.put(name, itemToAdd);
    }
}
