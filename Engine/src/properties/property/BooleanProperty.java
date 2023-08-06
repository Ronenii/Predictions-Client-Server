package properties.property;

public class BooleanProperty extends Property {
    private boolean value;

    public BooleanProperty(String name, boolean isRandInit, PropertyType type, boolean value) {
        super(name, isRandInit, type);
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Property{" +
                super.toString() +
                "value=" + value +
                '}';
    }
}
