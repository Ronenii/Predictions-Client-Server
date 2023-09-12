package simulation.objects.entity;

import simulation.properties.property.api.Property;

import java.io.Serializable;
import java.util.Map;

public class EntityInstance implements Serializable {
    private final Entity contextEntity;
    private final Map<String, Property> properties;
    private boolean isAlive;
    public int row;
    public int column;

    public EntityInstance(Map<String, Property> properties, Entity entityContext) {
        isAlive = true;
        this.properties = properties;
        this.contextEntity = entityContext;
    }

    public String getInstanceEntityName(){
        return contextEntity.getName();
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



    public void updateDerivedEntityInstance(EntityInstance killedInstance, int lastChangeTickCount) {
        Property newInstanceProperty;

        row = killedInstance.row;
        column = killedInstance.column;
        for (Property killedInstanceProperty : killedInstance.getProperties().values()) {
            newInstanceProperty = properties.get(killedInstanceProperty.getName());
            if(newInstanceProperty != null && newInstanceProperty.getType() == killedInstanceProperty.getType()) {
                newInstanceProperty.setValue(killedInstanceProperty.getValue(), lastChangeTickCount);
            }
        }
    }
}
