package simulation.properties.action.impl;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class DecreaseAction extends AbstractAction implements Serializable {
    private Object value;

    public DecreaseAction(String property, String contextEntity, String contextValue) {
        super(ActionType.DECREASE, property, contextEntity, contextValue);
    }

    @Override
    public void updateValue(UpdateObject updateObject) {
        OneObjectUpdate oneObjectUpdate = (OneObjectUpdate)updateObject;
        value = oneObjectUpdate.getObjectForUpdate();
    }

    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Decreases the value of a property based on the property's type.
     * Decrease actions can only take place on decimal or floats.
     * @param entityInstance The given entity to decrease the value of the action's property from.
     */
    @Override
    public void Invoke(EntityInstance entityInstance) {
        Property toDecrease = entityInstance.getPropertyByName(getContextProperty());

        if(toDecrease == null){
            return;
        }

        switch (toDecrease.getType())
        {
            case DECIMAL:
                toDecrease.setValue((int)toDecrease.getValue() - (int)value);
                break;
            case FLOAT:
                toDecrease.setValue((double)toDecrease.getValue() - (double)value);
                break;
        }
    }
}
