package properties.property.impl;

import properties.property.api.AbstractProperty;
import properties.property.api.PropertyType;

public class IntProperty extends AbstractProperty implements RangedProperty {
    private int from;
    private int to;

    public IntProperty(String name, boolean isRandInit, PropertyType type, Object value, int from, int to) {
        super(name, isRandInit, type, value);
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

}
