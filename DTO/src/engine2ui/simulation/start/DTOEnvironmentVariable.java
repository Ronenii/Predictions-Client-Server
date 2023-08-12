package engine2ui.simulation.start;

public class DTOEnvironmentVariable {
    private String name;
    private String type;
    private double from;
    private double to;

    public DTOEnvironmentVariable(String name, String type, double from, double to) {
        this.name = name;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\n", name) +
                String.format("Type: %s\n", type) +
                String.format("Range: %s-%s\n", from, to);
    }
}
