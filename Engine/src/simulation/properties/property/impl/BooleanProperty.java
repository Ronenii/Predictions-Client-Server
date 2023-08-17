package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.PropertyType;

public class BooleanProperty extends AbstractProperty {

    public BooleanProperty(String name, boolean isRandInit, Object value) {
        super(name, isRandInit, PropertyType.BOOLEAN, value);
    }

    /**
     * A constructor for Environment properties
     * @param name the property's name
     */
    public BooleanProperty(String name)
    {
        super(name, false, PropertyType.BOOLEAN, null);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
