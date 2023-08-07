package properties.property.impl;

import properties.property.api.AbstractProperty;
import properties.property.api.PropertyType;

public class BooleanProperty extends AbstractProperty {

    public BooleanProperty(String name, boolean isRandInit, PropertyType type, boolean value) {
        super(name, isRandInit, type, value);
    }
}
