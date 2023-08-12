package engine2ui.simulation.properties.property.impl;

import engine2ui.simulation.properties.property.api.DTOProperty;
import engine2ui.simulation.properties.property.api.RangedProperty;

public class RangedDTOProperty extends DTOProperty implements RangedProperty {
    private double from;
    private double to;

    public RangedDTOProperty(String name, String type, boolean isRandomInit, double from, double to) {
        super(name, type, isRandomInit);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(super.toString());
        ret.append(String.format("\tRange: %s-%s\n", from, to));

        return ret.toString();
    }
}
