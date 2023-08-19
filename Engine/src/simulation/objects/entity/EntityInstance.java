package simulation.objects.entity;

import simulation.properties.property.api.Property;

import java.io.Serializable;
import java.util.Map;

public class EntityInstance implements Serializable {
    private final Map<String, Property> properties;
    private boolean isAlive;

    public EntityInstance(Map<String, Property> properties) {
        isAlive = true;
        this.properties = properties;
    }

    public Property getPropertyByName(String propertyName) {
        return properties.get(propertyName);
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public void kill() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
