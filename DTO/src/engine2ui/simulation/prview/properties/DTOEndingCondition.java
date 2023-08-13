package engine2ui.simulation.prview.properties;

public class DTOEndingCondition {
    private final String type;
    private final int count;

    public DTOEndingCondition(String type, int count) {
        this.type = type;
        this.count = count;
    }

    @Override
    public String toString() {

        return String.format("Ending by: %s\n", type) +
                String.format("%s Limit: %s\n", type, count);
    }
}
