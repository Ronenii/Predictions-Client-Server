package engine2ui.simulation.properties;

import java.util.Arrays;
import java.util.List;

public class DTORule {
    String name;
    boolean ticks;
    boolean probability;
    List<String> actions;

    public DTORule(String name, boolean ticks, boolean probability, List<String> actions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }

    public DTORule(String name, boolean ticks, boolean probability, String... actions) {
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
}
