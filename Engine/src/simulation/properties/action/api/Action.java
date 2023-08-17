package simulation.properties.action.api;

import manager.value.update.object.api.UpdateObject;
import simulation.objects.entity.EntityInstance;

public interface Action {
    ActionType getType();

    String getContextEntity();

    String getContextProperty();

    void updateValue(UpdateObject updateObject);

    String getContextValue();

    Object getValue();

    void Invoke(EntityInstance entityInstance);

}
