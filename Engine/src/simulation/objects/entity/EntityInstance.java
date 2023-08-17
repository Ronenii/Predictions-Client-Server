package simulation.objects.entity;

import simulation.properties.property.api.Property;

public class EntityInstance {
    private final Property[] propertyValues;

    public EntityInstance(Property[] properties)
    {
        this.propertyValues = properties;
    }


}
