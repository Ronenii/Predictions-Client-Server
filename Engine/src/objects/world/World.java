package objects.world;

import objects.entity.Entity;
import objects.rule.Rule;
import properties.EndingCondition;
import properties.Property;

import java.util.Set;

public class World {

    private final Set<Property<?>> environmentProperties;
    private final Set<Entity> entities;

    private final Set<Rule> rules;
    private final Set<EndingCondition> endingConditions;

    public World(Set<Property<?>> environmentProperties, Set<Entity> entities, Set<Rule> rules, Set<EndingCondition> endingConditions) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
    }

    public Set<Property<?>>  getEnvironment() {
        return environmentProperties;
    }


    public Set<Entity> getEntities() {
        return entities;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public Set<EndingCondition> getEndingConditions() {
        return endingConditions;
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
