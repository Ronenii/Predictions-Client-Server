package properties.property;

public class StringProperty extends Property {
    private String value;

    public StringProperty(String name, boolean isRandInit, PropertyType type, String value) {
        super(name, isRandInit, type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Property{" +
                super.toString() +
                "value='" + value + '\'' +
                '}';
    }
}
