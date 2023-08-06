package objects;

import properties.Property;

import java.util.Set;

public class Entity {

    private int population;
    private String name;
    private Set<Property<?>> properties;

    public Entity(int population, String name, Set<Property<?>> properties) {
        this.population = population;
        this.name = name;
        this.properties = properties;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Property<?>> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property<?>> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        StringBuilder entityToString = new StringBuilder("Entity{" +
                "Name='" + name + '\'' +
                ", Population=" + population +
                ",Properties=");
        for (Property<?> p : properties
        ) {
            entityToString.append(p);
            entityToString.append(", ");
        }
        entityToString.append("}");
        return entityToString.toString();
    }
}
