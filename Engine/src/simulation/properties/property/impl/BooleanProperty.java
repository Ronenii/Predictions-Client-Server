package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.BoolRndValueGen;

import java.io.Serializable;

public class BooleanProperty extends AbstractProperty implements Serializable {

    public BooleanProperty(String name, boolean isRandInit, Object value, String entityName) {
        super(name, isRandInit, PropertyType.BOOLEAN, value, entityName);
    }

    /**
     * A constructor for Environment properties
     * @param name the property's name
     */
    public BooleanProperty(String name)
    {
        super(name, false, PropertyType.BOOLEAN, null, null);
    }

    @Override
    public void setValue(Object value, int lastChangTickCount) {
        if(this.value != value){
            this.value = value;
            this.lastChangeTickCount = lastChangTickCount;
            this.changeTickAmount++;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public Property dupProperty() {
        return new BooleanProperty(getName(),isRandInit(),getValue(), getEntityName());
    }

    @Override
    public Property generateRandomValueProperty() {
        boolean value;

        RandomValueGenerator<Boolean> randomValueGenerator = new BoolRndValueGen();
        value = randomValueGenerator.generateRandomValue();

        return new BooleanProperty(getName(),isRandInit(), value, getEntityName());
    }
}
