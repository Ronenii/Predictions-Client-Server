package simulation.properties.property.api;

import java.io.Serializable;

public abstract class AbstractProperty implements Property, Serializable {
    private final String name;
    private boolean isRandInit;
    private final PropertyType type;
    protected Object value;
    protected int lastChangeTickCount;


    public AbstractProperty(String name, boolean isRandInit, PropertyType type, Object value) {
        this.name = name;
        this.isRandInit = isRandInit;
        this.type = type;
        this.value = value;
        this.lastChangeTickCount = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PropertyType getType() {
        return type;
    }

    @Override
    public Boolean isRandInit() {
        return isRandInit;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public int getLastChangeTickCount() {
        return lastChangeTickCount;
    }

    @Override
    public void updateValueAndIsRandomInit(Object value, boolean isRandomInit) {
        this.value = value;
        this.isRandInit = isRandomInit;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", isRandInit=" + isRandInit +
                ", type=" + type +
                '}';
    }

    @Override
    public int hashCode() {
        return name.length() * (type.ordinal() + 1) * Boolean.toString(isRandInit).length();
    }

    @Override
    public boolean equals(Object obj) {
        Property toCompare = (Property) obj;
        return (toCompare.isRandInit() == this.isRandInit) && (toCompare.getName().equals(this.name)) && (toCompare.getType().equals((this.type))) && (toCompare.getValue().equals(this.value));
    }
}
