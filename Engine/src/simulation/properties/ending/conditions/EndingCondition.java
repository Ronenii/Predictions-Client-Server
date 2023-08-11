package simulation.properties.ending.conditions;

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
}
