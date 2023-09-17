package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.StringRndValueGen;

import java.io.Serializable;

public class StringProperty extends AbstractProperty implements Serializable {

    public StringProperty(String name, boolean isRandInit, Object value, String entityName) {
        super(name, isRandInit, PropertyType.STRING, value, entityName);
    }

    /**
     * A constructor for Environment properties
     * @param name the property's name
     */
    public StringProperty(String name) {
        super(name, false, PropertyType.STRING, null, null);
    }

    @Override
    public void setValue(Object value, int lastChangTickCount) {
        if(this.value.toString().equals(value.toString())) {
            this.value = value;
            this.lastChangeTickCount = lastChangTickCount;
            this.changeTickAmount++;
        }
    }

    @Override
    public Property dupProperty() {
        return new StringProperty(getName(),isRandInit(), getValue(), getEntityName());
    }

    @Override
    public Property generateRandomValueProperty() {
        String value;

        RandomValueGenerator<String> randomValueGenerator = new StringRndValueGen();
        value = randomValueGenerator.generateRandomValue();

        return new StringProperty(getName(),isRandInit(), value, getEntityName());
    }

}
