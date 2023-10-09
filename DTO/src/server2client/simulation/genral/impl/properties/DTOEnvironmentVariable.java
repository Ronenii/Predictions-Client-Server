package server2client.simulation.genral.impl.properties;

public class DTOEnvironmentVariable {
    private final String name;
    private final String type;
    private final Double from;
    private final Double to;

    public DTOEnvironmentVariable(String name, String type, Double from, Double to) {
        this.name = name;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\n", name) +
                String.format("Type: %s\n", type) +
                String.format("Range: %s-%s\n", from, to);
    }
}
