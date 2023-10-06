package server2client.simulation.genral.impl.properties.property.api;

import java.io.Serializable;

abstract public class DTOProperty implements Serializable {
    private final String name;
    private final String type;
    private final boolean isRandomInit;
    private final Object value;
    private final int changeTickAmount;

    public DTOProperty(String name, String type, boolean isRandomInit, Object value, int changeTickAmount) {
        this.name = name;
        this.type = type;
        this.isRandomInit = isRandomInit;
        this.value = value;
        this.changeTickAmount = changeTickAmount;
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

    public int getChangeTickAmount() {
        return changeTickAmount;
    }

    @Override
    public String toString() {

        return String.format("\tName: %s\n", name) +
                String.format("\tType: %s\n", type) +
                String.format("\tRandomly initialized: %s\n", isRandomInit);
    }
}
