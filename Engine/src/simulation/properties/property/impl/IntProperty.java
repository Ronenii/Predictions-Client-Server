package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.DoubleRndValueGen;
import simulation.properties.property.random.value.impl.IntRndValueGen;

public class IntProperty extends AbstractProperty implements RangedProperty {
    private int from;
    private int to;

    public IntProperty(String name, boolean isRandInit, Object value, int from, int to) {
        super(name, isRandInit, PropertyType.DECIMAL, value);
        this.from = from;
        this.to = to;
    }

    /**
     * A constructor for Environment properties
     * @param name the property's name
     */
    public IntProperty(String name, int from, int to)
    {
        super(name, false, PropertyType.DECIMAL, null);
        this.from = from;
        this.to = to;
    }

    @Override
    public void setValue(Object value) {
        int givenValue = (int)value;

        if(givenValue < from){
            this.value = from;
        } else if (givenValue > to) {
            this.value = to;
        }
        else {
            this.value = value;
        }
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public Property dupProperty() {
        return new IntProperty(getName(),isRandInit(), getValue(), from, to);
    }

    @Override
    public Property generateRandomValueProperty() {
        int value;

        RandomValueGenerator<Integer> randomValueGenerator = new IntRndValueGen(from, to);
        value = randomValueGenerator.generateRandomValue();

        return new IntProperty(getName(),isRandInit(), value, from, to);
    }

}
