package simulation.objects.entity;

import simulation.properties.property.api.Property;

import java.util.Map;

public class Entity {

    private final int population;
    private final String name;
    private final Map<String,Property> properties;

    public Entity(int population, String name, Map<String, Property> properties) {
        this.population = population;
        this.name = name;
        this.properties = properties;
    }

    public int getPopulation() {
        return population;
    }

    public String getName() {
        return name;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder entityToString = new StringBuilder("Entity{" +
                "Name='" + name + '\'' +
                ", Population=" + population +
                ",Properties=[");
        for (Property p : properties.values()
        ) {
            int counter = 0;
            entityToString.append(p);
            if (++counter != properties.size())
                entityToString.append(", ");
            else
                entityToString.append(']');
        }
        entityToString.append("}");
        return entityToString.toString();
    }
}
