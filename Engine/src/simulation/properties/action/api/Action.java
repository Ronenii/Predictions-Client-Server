package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;



public interface Action {
    ActionType getType();

    String getContextEntity();

    String getContextProperty();

    Object getValue();


}
