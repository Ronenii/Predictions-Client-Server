package simulation.properties.rule;

import simulation.objects.entity.Entity;
import simulation.properties.action.api.Action;
import simulation.properties.action.impl.condition.MultipleCondition;
import simulation.properties.activition.Activation;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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

    /**
     * Invokes this rule on all world entities.
     * Checks if the required amount of ticks has passed.
     * Will then try to invoke all of this rule's actions on each entity.
     *
     * @param entities The world's entities that we invoke the rule on
     */
    public void invokeRuleOnWorldEntities(Collection<Entity> entities, int lastChangTickCount) {
        if (willInvoke()) {
            for (Entity e: entities
                 ) {
                invokeActionsOnEntity(e, lastChangTickCount);
            }
        }
    }

    /**
     * Iterates through all actions in the list, if the given entity is an entity that the action
     * is designed for then invoke said action on all instances of this entity.
     * @param entity the given entity to invoke the actions upon.
     */
    private void invokeActionsOnEntity(Entity entity, int lastChangTickCount){
        for (Action a: actions
        ) {
            if(a.getClass() == MultipleCondition.class || a.getContextEntity().equals(entity.getName()))
            {
                entity.invokeActionOnAllInstances(a, activation.getProbability(), lastChangTickCount);
            }
        }
    }

    /**
     * @return true if the required amount of ticks has passed since last invoking the rule.
     */
    private boolean willInvoke() {
        return (simulationTickCount++ % activation.getTicks()) == 0;
    }
}
