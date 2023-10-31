package server2client.simulation.genral.impl.properties;

import java.io.Serializable;

public class DTOProperty implements Serializable {
    private final String name;
    private final String type;
    private final boolean isRandomInit;
    private final Object value;
    private final boolean hasRange;
    private double from;
    private double to;
    private final int changeTickAmount;

    public DTOProperty(String name, String type, boolean isRandomInit, Object value, int changeTickAmount, boolean hasRange) {
        this.name = name;
        this.type = type;
        this.isRandomInit = isRandomInit;
        this.value = value;
        this.changeTickAmount = changeTickAmount;
        this.hasRange = hasRange;
    }

    public DTOProperty(String name, String type, boolean isRandomInit, Object value, double from, double to, int changeTickAmount, boolean hasRange) {
        this.name = name;
        this.type = type;
        this.isRandomInit = isRandomInit;
        this.value = value;
        this.hasRange = hasRange;
        this.from = from;
        this.to = to;
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

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    public boolean isHasRange() {
        return hasRange;
    }

    @Override
    public String toString() {

        return String.format("\tName: %s\n", name) +
                String.format("\tType: %s\n", type) +
                String.format("\tRandomly initialized: %s\n", isRandomInit);
    }
}
