package simulation.properties.rule;

import simulation.objects.entity.Entity;
import simulation.properties.action.api.Action;
import simulation.properties.action.impl.condition.MultipleCondition;
import simulation.properties.activition.Activation;

import java.io.Serializable;
import java.util.*;

public class Rule implements Serializable {
    private final String name;
    private final Activation activation;
    private final List<Action> actions;

    private int simulationTickCount;

    public Rule(String name, Activation activation, List<Action> actions) {
        this.name = name;
        this.activation = activation;
        this.actions = actions;
        simulationTickCount = 0;
    }

    public String getName() {
        return name;
    }

    public Activation getActivation() {
        return activation;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void updateTicks() {
        simulationTickCount++;
    }

    @Override
    public String toString() {
        StringBuilder ruleToString = new StringBuilder("Rule{" +
                "name='" + name + '\'' +
                ", activation=" + activation +
                ", actions=[");

        for (Action a : actions
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
        Rule toCompare = (Rule) obj;
        return toCompare.name.equals(this.name) && toCompare.activation.equals(this.activation) && toCompare.actions.equals(actions);
    }


    public List<Action> getActionsToInvoke() {
        List<Action> actionsToInvoke = new ArrayList<>();

        if(willInvokeByTicks() && willInvokeByProbability()) {
            actionsToInvoke.addAll(actions);
        }

        return actionsToInvoke;
    }

    /**
     * @return true if the required amount of ticks has passed since last invoking the rule.
     */
    private boolean willInvokeByTicks() {
        return (simulationTickCount++ % activation.getTicks()) == 0;
    }

    private boolean willInvokeByProbability() {
        double min = 0.0;
        double max = 1.0;
        Random random = new Random();
        // Generate a random double within the specified inclusive range
        double randomValue = min + (max - min + Double.MIN_VALUE) * random.nextDouble();

        return randomValue <= activation.getProbability();
    }
}
