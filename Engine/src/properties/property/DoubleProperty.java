package properties.property;

public class DoubleProperty extends Property implements RangedProperty {
    private double value;
    private double from;
    private double to;

    public DoubleProperty(String name, boolean isRandInit, PropertyType type, double value, double from, double to) {
        super(name, isRandInit, type);
        this.value = value;
        this.from = from;
        this.to = to;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getFrom() {
        return from;
    }

    public void setFrom(double from) {
        this.from = from;
    }

    public double getTo() {
        return to;
    }

    public void setTo(double to) {
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
