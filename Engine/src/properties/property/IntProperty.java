package properties.property;

public class IntProperty extends Property implements RangedProperty {
    private int value;
    private int from;
    private int to;

    public IntProperty(String name, boolean isRandInit, PropertyType type, int value, int from, int to) {
        super(name, isRandInit, type);
        this.value = value;
        this.from = from;
        this.to = to;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Property{" +
                super.toString() +
                "value=" + value +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
