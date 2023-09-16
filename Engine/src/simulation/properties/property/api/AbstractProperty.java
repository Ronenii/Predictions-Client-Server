package simulation.properties.property.api;

import java.io.Serializable;

public abstract class AbstractProperty implements Property, Serializable {
    private final String name;
    private final String entityName;
    private boolean isRandInit;
    private final PropertyType type;
    protected Object value;
    protected int lastChangeTickCount;
    protected int changeTickAmount;

    public AbstractProperty(String name, boolean isRandInit, PropertyType type, Object value, String entityName) {
        this.name = name;
        this.entityName = entityName;
        this.isRandInit = isRandInit;
        this.type = type;
        this.value = value;
        this.lastChangeTickCount = 0;
        this.changeTickAmount = 1;
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
    public String getEntityName() {
        return entityName;
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
    public int getChangeTickAmount() {
        return changeTickAmount;
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
