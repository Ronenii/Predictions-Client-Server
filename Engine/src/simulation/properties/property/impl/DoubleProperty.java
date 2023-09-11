package simulation.properties.property.impl;

import simulation.properties.property.api.AbstractProperty;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.BoolRndValueGen;
import simulation.properties.property.random.value.impl.DoubleRndValueGen;

import java.io.Serializable;

public class DoubleProperty extends AbstractProperty implements RangedProperty, Serializable {
    private final double from;
    private final double to;

    public DoubleProperty(String name, boolean isRandInit, Object value, double from, double to, String entityName) {
        super(name, isRandInit, PropertyType.FLOAT, value, entityName);
        this.from = from;
        this.to = to;
    }

    /**
     * A constructor for Environment properties
     * @param name the property's name
     */
    public DoubleProperty(String name, double from, double to)
    {
        super(name, false, PropertyType.FLOAT, null, null);
        this.from = from;
        this.to = to;
    }

    @Override
    public void setValue(Object value, int lastChangTickCount) {
        double givenValue = (double)value;

        if(givenValue < from){
            this.value = from;
        } else if (givenValue > to) {
            this.value = to;
        }
        else {
            this.value = value;
        }

        this.lastChangeTickCount = lastChangTickCount;
    }


    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    @Override
    public Property dupProperty() {
        return new DoubleProperty(getName(),isRandInit(), getValue(), from, to, getEntityName());
    }

    @Override
    public Property generateRandomValueProperty() {
        double value;

        RandomValueGenerator<Double> randomValueGenerator = new DoubleRndValueGen(from, to);
        value = randomValueGenerator.generateRandomValue();

        return new DoubleProperty(getName(),isRandInit(), value, from, to, getEntityName());
    }
}
