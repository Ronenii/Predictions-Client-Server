package simulation.objects.entity;

import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.property.api.Property;

import java.io.Serializable;
import java.util.*;

public class Entity implements Serializable {

    private int startingPopulation;
    private final String name;
    private final Map<String, Property> properties;
    private final List<EntityInstance> entityInstances;

    public Entity(String name, Map<String, Property> properties) {
        this.startingPopulation = 0;
        this.name = name;
        this.properties = properties;
        this.entityInstances = new ArrayList<>();
    }

    public Entity(String name, Map<String, Property> properties, int startingPopulation) {
        this.startingPopulation = startingPopulation;
        this.name = name;
        this.properties = properties;
        this.entityInstances = new ArrayList<>();
    }

    public int getStartingPopulation() {
        return startingPopulation;
    }

    public void setStartingPopulation(int startingPopulation) {
        this.startingPopulation = startingPopulation;
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
                ", Population=" + startingPopulation +
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

    public List<EntityInstance> getEntityInstances() {
        return entityInstances;
    }

    public int getCurrentPopulation() {
        int aliveCount = 0;
        for (EntityInstance e : entityInstances
        ) {
            if (e.isAlive()) {
                aliveCount++;
            }
        }

        return aliveCount;
    }

    public void resetPopulation() {
        entityInstances.clear();

        for (int i = 0; i < this.startingPopulation; i++) {
            entityInstances.add(new EntityInstance(generateProperties(), this));
        }
    }

    public Map<String, Property> generateProperties() {
        Map<String, Property> propertyMap = new HashMap<>();

        for (Property property : properties.values()) {
            if (property.isRandInit()) {
                propertyMap.put(property.getName(), property.generateRandomValueProperty());
            } else {
                propertyMap.put(property.getName(), property.dupProperty());
            }
        }

        return propertyMap;
    }

    public EntityInstance createNewEntityInstance() {
        EntityInstance ret = new EntityInstance(generateProperties(), this);

        entityInstances.add(ret);
        return ret;
    }

    @Override
    public int hashCode() {
        return name.length() * startingPopulation * properties.size();
    }

    public Entity dupEntity() {
        Map<String, Property> propertyMap = new HashMap<>();

        for (Property property : properties.values()) {
            propertyMap.put(property.getName(), property.dupProperty());
        }
        return new Entity(name, propertyMap, startingPopulation);
    }

    public EntityInstance getRandomEntityInstance() {
        Random random = new Random();
        int randomIndex = random.nextInt(entityInstances.size());

        return entityInstances.get(randomIndex);
    }

    @Override
    public boolean equals(Object obj) {
        Entity toCompare = (Entity) obj;

        return toCompare.getName().equals(this.name) && toCompare.getStartingPopulation() == this.startingPopulation && toCompare.getProperties().equals(this.properties);
    }


}
