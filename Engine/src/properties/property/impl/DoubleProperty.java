package properties.property.impl;

import properties.property.api.AbstractProperty;
import properties.property.api.PropertyType;

public class DoubleProperty extends AbstractProperty implements RangedProperty {
    private double from;
    private double to;

    public DoubleProperty(String name, boolean isRandInit, PropertyType type, Object value, double from, double to) {
        super(name, isRandInit, type, value);
        this.from = from;
        this.to = to;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

}
