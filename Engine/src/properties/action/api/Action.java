package properties.action.api;

import objects.entity.Entity;
import properties.property.api.Property;

public interface Action {
    ActionType getType();

    Entity getContextEntity();

    Property getProperty();

    void Invoke();

}
