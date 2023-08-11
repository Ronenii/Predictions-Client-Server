package simulation.properties.property.api;

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
    public Boolean isRandInit() {return isRandInit;}

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", isRandInit=" + isRandInit +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
