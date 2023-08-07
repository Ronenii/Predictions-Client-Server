package objects.entity;

import properties.property.api.Property;

import java.util.Set;

public class Entity {

    private final int population;
    private final String name;
    private final Set<Property> properties;

    public Entity(int population, String name, Set<Property> properties) {
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

    public Set<Property> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder entityToString = new StringBuilder("Entity{" +
                "Name='" + name + '\'' +
                ", Population=" + population +
                ",Properties=[");
        for (Property p : properties
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
