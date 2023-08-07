package properties.property.impl;

import properties.property.api.AbstractProperty;
import properties.property.api.PropertyType;

public class StringProperty extends AbstractProperty {

    public StringProperty(String name, boolean isRandInit, PropertyType type, Object value) {
        super(name, isRandInit, type, value);
    }

}
