package objects;

import objects.env.Environment;
import properties.EndingCondition;

import java.util.Set;

public class World {

    private Environment environment;
    private Set<Entity> entities;

    private Set<Rule> rules;
    private Set<EndingCondition> endingConditions;

    public World(Environment environment, Set<Entity> entities, Set<Rule> rules, Set<EndingCondition> endingConditions) {
        this.environment = environment;
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public void setEntities(Set<Entity> entities) {
        this.entities = entities;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

    public Set<EndingCondition> getEndingConditions() {
        return endingConditions;
    }

    public void setEndingConditions(Set<EndingCondition> endingConditions) {
        this.endingConditions = endingConditions;
    }

    @Override
    public String toString() {
        StringBuilder worldToString = new StringBuilder("World{" +
                "environment=" + environment +
                ", entities=");
        for (Entity e : entities) {
            int counter = 0;
            worldToString.append(e);
            if (++counter != entities.size())
                worldToString.append(", ");
        }

        worldToString.append(", rules=");
        for (Rule r : rules) {
            int counter = 0;
            worldToString.append(r);
            if (++counter != rules.size())
                worldToString.append(", ");
        }

        worldToString.append(", endingConditions=");
        for (EndingCondition e : endingConditions) {
            int counter = 0;
            worldToString.append(e);
            if (++counter != endingConditions.size())
                worldToString.append(", ");
        }

        worldToString.append('}');
        return  worldToString.toString();
    }
}
