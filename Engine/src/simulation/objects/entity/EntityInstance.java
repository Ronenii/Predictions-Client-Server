package simulation.objects.entity;

import simulation.properties.property.api.Property;

import java.util.Map;

public class EntityInstance {
    private final Map<String, Property> propertyValues;

    private boolean isAlive;

    public EntityInstance(Map<String, Property>  properties)
    {
        isAlive = true;
        this.propertyValues = properties;
    }

    public Property getPropertyByName(String propertyName){
        return propertyValues.get(propertyName);
    }

    public void kill(){
        isAlive = false;
    }
}
