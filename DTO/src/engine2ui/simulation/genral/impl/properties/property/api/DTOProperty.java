package engine2ui.simulation.genral.impl.properties.property.api;

abstract public class DTOProperty {
    private final String name;
    private final String type;
    private final boolean isRandomInit;

    private final Object value;
    public DTOProperty(String name, String type, boolean isRandomInit, Object value) {
        this.name = name;
        this.type = type;
        this.isRandomInit = isRandomInit;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isRandomInit() {
        return isRandomInit;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {

        return String.format("\tName: %s\n", name) +
                String.format("\tType: %s\n", type) +
                String.format("\tRandomly initialized: %s\n", isRandomInit);
    }
}
