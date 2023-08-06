package properties.property;

import javafx.util.Pair;

public abstract class Property {
    protected String name;
    protected boolean isRandInit;
    protected PropertyType type;

    public Property(String name, boolean isRandInit, PropertyType type) {
        this.name = name;
        this.isRandInit = isRandInit;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRandInit() {
        return isRandInit;
    }

    public void setRandInit(boolean randInit) {
        isRandInit = randInit;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", isRandInit=" + isRandInit +
                ", type=" + type;
    }
}
