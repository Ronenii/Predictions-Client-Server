package properties.property.impl;

import properties.property.api.AbstractProperty;
import properties.property.api.PropertyType;

public class IntProperty extends AbstractProperty implements RangedProperty {
    private int from;
    private int to;

    public IntProperty(String name, boolean isRandInit, Object value, int from, int to) {
        super(name, isRandInit, PropertyType.INT, value);
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
