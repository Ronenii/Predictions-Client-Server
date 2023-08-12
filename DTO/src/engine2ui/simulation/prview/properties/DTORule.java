package engine2ui.simulation.prview.properties;

import java.util.List;

public class DTORule {
    String name;
    int ticks;
    double probability;
    List<String> actions;

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
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("Name: %s\n", name));
        ret.append(String.format("Invoke every: %s ticks\n", ticks));
        ret.append(String.format("Probability of successful invoke: %s\n", probability * 100.0 ));
        ret.append(String.format("Number of actions: %s\n", actions.size()));
        ret.append("Actions: \n");
        for(int i = 1; i<= actions.size(); i++){
            ret.append(String.format("#%s\t ",i));
            ret.append(actions.get(i-1));
            ret.append("\n");
        }

        return ret.toString();
    }
}
