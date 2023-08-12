package engine2ui.simulation.prview.properties;

import engine2ui.simulation.genral.HasList;

import java.util.List;

public class DTORule implements HasList {
    private final String name;
    private final int ticks;
    private final double probability;
    private List<String> actions;

    public DTORule(String name, int ticks, double probability, List<String> actions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }

    public DTORule(String name, int ticks, double probability, String... actions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        for (String a: actions
             ) {
            addAction(a);
        }
    }

    public void addAction(String actionName)
    {
        actions.add(actionName);
    }

    @Override
    public String toString() {

        return String.format("Name: %s\n", name) +
                String.format("Invoke every: %s ticks\n", ticks) +
                String.format("Probability of successful invoke: %s\n", probability * 100.0) +
                String.format("Number of actions: %s\n", actions.size()) +
                "Actions: \n" +
                formatListToString(actions);
    }
}
