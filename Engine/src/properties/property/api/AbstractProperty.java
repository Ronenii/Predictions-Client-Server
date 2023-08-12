package properties.property.api;

public abstract class AbstractProperty implements Property {
    private String name;
    private boolean isRandInit;
    private PropertyType type;
    private Object value;

    public AbstractProperty(String name, boolean isRandInit, PropertyType type, Object value) {
        this.name = name;
        this.isRandInit = isRandInit;
        this.type = type;
        this.value = value;
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
    public Object getValue() {
        return value;
    }

    @Override
    public void updateValue(Object value) {
        this.value = value;
    }

    @Override
    public Boolean isRandInit() {
        return isRandInit;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", isRandInit=" + isRandInit +
                ", type=" + type +
                ", value=" + value +
                '}';
    }

    @Override
    public int hashCode() {
        return name.length() * (type.ordinal() + 1) * Boolean.toString(isRandInit).length();
    }

    @Override
    public boolean equals(Object obj) {
        Property toCompare = (Property) obj;
        return (toCompare.isRandInit() == this.isRandInit) && (toCompare.getName().equals(this.name)) && (toCompare.getType().equals((this.type))) && (toCompare.getValue().equals(this.type));
    }
}
