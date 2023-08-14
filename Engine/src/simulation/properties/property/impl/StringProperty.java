package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.PropertyType;

public class StringProperty extends AbstractProperty {

    public StringProperty(String name, boolean isRandInit, Object value) {
        super(name, isRandInit, PropertyType.STRING, value);
    }

    /**
     * A constructor for Environment properties
     * @param name the property's name
     */
    public StringProperty(String name) {
        super(name, false, PropertyType.STRING, "");
    }

}
