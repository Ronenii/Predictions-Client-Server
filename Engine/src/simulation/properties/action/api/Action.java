package simulation.properties.action.api;

import manager.value.update.object.api.UpdateObject;

public interface Action {
    ActionType getType();

    String getContextEntity();

    String getProperty();

    void updateValue(UpdateObject updateObject);

    String getContextValue();

    void Invoke();

}
