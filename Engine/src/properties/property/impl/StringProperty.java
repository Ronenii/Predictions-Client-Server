package properties.property.impl;

import properties.property.api.AbstractProperty;
import properties.property.api.PropertyType;

public class StringProperty extends AbstractProperty {

    public StringProperty(String name, boolean isRandInit, Object value) {
        super(name, isRandInit, PropertyType.STRING, value);
    }

}
