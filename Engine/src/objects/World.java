package objects;

import properties.EndingCondition;
import properties.Property;

import java.util.Set;

public class World {

    private Set<Property<?>> environmentProperties;
    private Set<Entity> entities;

    private Set<Rule> rules;
    private Set<EndingCondition> endingConditions;

    public World(Set<Property<?>> environmentProperties, Set<Entity> entities, Set<Rule> rules, Set<EndingCondition> endingConditions) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
    }

    public Set<Property<?>>  getEnvironment() {
        return environmentProperties;
    }

    public void setEnvironment(Set<Property<?>>  environment) {
        this.environmentProperties = environment;
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
                "environmentProperties=[");
        for (Property<?> p : environmentProperties) {
            int counter = 0;
            worldToString.append(p);
            if (++counter != environmentProperties.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", entities=[");
        for (Entity e : entities) {
            int counter = 0;
            worldToString.append(e);
            if (++counter != entities.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", rules=[");
        for (Rule r : rules) {
            int counter = 0;
            worldToString.append(r);
            if (++counter != rules.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", endingConditions=[");
        for (EndingCondition e : endingConditions) {
            int counter = 0;
            worldToString.append(e);
            if (++counter != endingConditions.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append('}');
        return  worldToString.toString();
    }
}
