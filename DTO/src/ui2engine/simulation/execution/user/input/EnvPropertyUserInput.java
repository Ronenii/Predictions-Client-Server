package ui2engine.simulation.execution.user.input;

public class EnvPropertyUserInput {
    private final String name;
    private final boolean isRandomInit;
    private final Object value;

    public EnvPropertyUserInput(String name, boolean isRandomInit, Object value) {
        this.name = name;
        this.isRandomInit = isRandomInit;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean isRandomInit() {
        return isRandomInit;
    }

    public Object getValue() {
        return value;
    }
}
