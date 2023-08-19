package engine2ui.simulation.genral.impl.properties.property.impl;

import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.genral.impl.properties.property.api.RangedProperty;

public class RangedDTOProperty extends DTOProperty implements RangedProperty {
    private final double from;
    private final double to;

    public RangedDTOProperty(String name, String type, boolean isRandomInit,  Object value, double from, double to) {
        super(name, type, isRandomInit,value);
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
