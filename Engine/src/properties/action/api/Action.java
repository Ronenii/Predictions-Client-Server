package properties.action.api;

import objects.entity.Entity;
import properties.property.api.Property;

public interface Action {
    ActionType getType();

    String getContextEntity();

    String getProperty();

    void Invoke();

}
