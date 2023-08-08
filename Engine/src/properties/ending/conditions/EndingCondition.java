package properties.ending.conditions;

public class EndingCondition {
    private EndingConditionType type;
    private int count;

    public EndingCondition(EndingConditionType type, int count) {
        this.type = type;
        this.count = count;
    }

    public EndingConditionType getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        return (type.ordinal() + 1) * (count * (type.ordinal() + 1));
    }

    @Override
    public boolean equals(Object obj) {
        EndingCondition toCompare = (EndingCondition) obj;
        return (toCompare.count == this.count) && (toCompare.type == this.type);
    }
}
