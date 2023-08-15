package ui2engine.simulation.func3.user.input;

public class EnvPropertyUserInput {
    private String name;
    private boolean isRandomInit;
    private Object value;

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
