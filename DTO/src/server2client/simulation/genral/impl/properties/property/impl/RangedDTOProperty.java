package server2client.simulation.genral.impl.properties.property.impl;

import server2client.simulation.genral.impl.properties.property.api.DTOProperty;
import server2client.simulation.genral.impl.properties.property.api.RangedProperty;

public class RangedDTOProperty extends DTOProperty implements RangedProperty {
    private final double from;
    private final double to;

    public RangedDTOProperty(String name, String type, boolean isRandomInit, Object value, int changeTickAmount, double from, double to) {
        super(name, type, isRandomInit, value, changeTickAmount);
        this.from = from;
        this.to = to;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\tRange: %s-%s\n", from, to);
    }
}
