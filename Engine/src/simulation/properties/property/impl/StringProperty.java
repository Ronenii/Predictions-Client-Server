package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.IntRndValueGen;
import simulation.properties.property.random.value.impl.StringRndValueGen;

import java.io.Serializable;

public class StringProperty extends AbstractProperty implements Serializable {

    public StringProperty(String name, boolean isRandInit, Object value) {
        super(name, isRandInit, PropertyType.STRING, value);
    }

    /**
     * A constructor for Environment properties
     * @param name the property's name
     */
    public StringProperty(String name) {
        super(name, false, PropertyType.STRING, null);
    }

    @Override
    public void setValue(Object value, int lastChangTickCount) {
        this.value = value;
        this.lastChangeTickCount = lastChangTickCount;
    }

    @Override
    public Property dupProperty() {
        return new StringProperty(getName(),isRandInit(), getValue());
    }

    @Override
    public Property generateRandomValueProperty() {
        String value;

        RandomValueGenerator<String> randomValueGenerator = new StringRndValueGen();
        value = randomValueGenerator.generateRandomValue();

        return new StringProperty(getName(),isRandInit(), value);
    }

}
