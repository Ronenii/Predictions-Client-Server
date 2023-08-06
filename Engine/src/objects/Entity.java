package objects;

import properties.Property;

import java.util.Map;

public class Entity {

    private int population;
    private String name;
    private Map<Integer, Property<?>> properties;

    public Entity(int population, String name, Map<Integer, Property<?>> properties) {
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

    public Map<Integer, Property<?>> getProperties() {
        return properties;
    }

    public void setProperties(Map<Integer, Property<?>> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        StringBuilder entityToString = new StringBuilder("Entity{" +
                "Name='" + name + '\'' +
                ", Population='" + '\'' +
                ",Properties='");
        for (Property<?> p : properties.values()
        ) {
            entityToString.append(p.toString());
        }
        entityToString.append("'}");
        return entityToString.toString();
    }
}
