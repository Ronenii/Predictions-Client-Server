package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.PropertyType;

public class DoubleProperty extends AbstractProperty implements RangedProperty {
    private double from;
    private double to;

    public DoubleProperty(String name, boolean isRandInit, Object value, double from, double to) {
        super(name, isRandInit, PropertyType.DOUBLE, value);
        this.from = from;
        this.to = to;
    }

    /**
     * A constructor for Environment properties
     * @param name the property's name
     */
    public DoubleProperty(String name, double from, double to)
    {
        super(name, false, PropertyType.DOUBLE, 0.0);
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
