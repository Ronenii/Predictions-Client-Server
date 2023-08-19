package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.BoolRndValueGen;

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
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public Property dupProperty() {
        return new BooleanProperty(getName(),isRandInit(),getValue());
    }

    @Override
    public Property generateRandomValueProperty() {
        boolean value;

        RandomValueGenerator<Boolean> randomValueGenerator = new BoolRndValueGen();
        value = randomValueGenerator.generateRandomValue();

        return new BooleanProperty(getName(),isRandInit(), value);
    }
}
