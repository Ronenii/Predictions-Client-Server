package objects.rule;

import properties.action.api.Action;
import properties.activition.Activation;

import java.util.Map;
import java.util.Set;

public class Rule {
    private final String name;
    private final Activation activation;
    private final Set<Action> actions;

    public Rule(String name, Activation activation, Set<Action> actions) {
        this.name = name;
        this.activation = activation;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public Activation getActivation() {
        return activation;
    }

    public Set<Action> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        StringBuilder ruleToString = new StringBuilder("Rule{" +
                "name='" + name + '\'' +
                ", activation=" + activation +
                ", actions=[");

        for (Action a : actions.values()
        ) {
            int counter = 0;
            ruleToString.append(a);
            if (++counter != actions.size())
                ruleToString.append(", ");
            else
                ruleToString.append(']');
        }
        ruleToString.append('}');
        return ruleToString.toString();
    }

    @Override
    public int hashCode() {
        return name.length() * getActions().size() * activation.getTicks();
    }

    @Override
    public boolean equals(Object obj) {
        Rule toCompare = (Rule)obj;
        return toCompare.name.equals(this.name) && toCompare.activation.equals(this.activation) && toCompare.actions.equals(actions);
    }
}
