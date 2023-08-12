package objects.world;

import objects.entity.Entity;
import objects.rule.Rule;
import properties.ending.conditions.EndingCondition;
import properties.property.api.Property;

import java.util.Map;
import java.util.Set;

public class World {

    private final Map<String, Property> environmentProperties;
    private final Map<String, Entity> entities;

    private final Map<String, Rule> rules;
    private final Map<String, EndingCondition> endingConditions;

    public World(Map<String, Property> environmentProperties, Map<String, Entity> entities, Map<String, Rule> rules, Map<String, EndingCondition> endingConditions) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
    }



    @Override
    public String toString() {
        StringBuilder worldToString = new StringBuilder("World{" +
                "environmentProperties=[");
        for (Property p : environmentProperties.values()) {
            int counter = 0;
            worldToString.append(p);
            if (++counter != environmentProperties.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", entities=[");
        for (Entity e : entities.values()) {
            int counter = 0;
            worldToString.append(e);
            if (++counter != entities.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", rules=[");
        for (Rule r : rules.values()) {
            int counter = 0;
            worldToString.append(r);
            if (++counter != rules.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", endingConditions=[");
        for (EndingCondition e : endingConditions.values()) {
            int counter = 0;
            worldToString.append(e);
            if (++counter != endingConditions.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append('}');
        return worldToString.toString();
    }

    @Override
    public int hashCode() {
        return environmentProperties.size() * entities.size() * rules.size() * endingConditions.size();
    }

    @Override
    public boolean equals(Object obj) {
        World toCompare = (World) obj;
        return (toCompare.endingConditions.equals(this.endingConditions)) && (toCompare.rules.equals(this.rules)) && (toCompare.entities.equals(entities)) && (toCompare.environmentProperties.equals(this.environmentProperties));
    }
}
