package server2client.simulation.genral.impl.properties;

public class DTOEndingCondition {
    private final String type;
    private final int count;

    public DTOEndingCondition(String type, int count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {

        return String.format("Ending by: %s\n", type) +
                String.format("%s Limit: %s\n", type, count);
    }
}
