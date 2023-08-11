package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.PropertyType;

public class BooleanProperty extends AbstractProperty {

    public BooleanProperty(String name, boolean isRandInit, boolean value) {
        super(name, isRandInit, PropertyType.BOOLEAN, value);
    }
}
