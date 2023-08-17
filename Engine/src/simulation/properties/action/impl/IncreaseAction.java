package simulation.properties.action.impl;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.property.api.Property;

public class IncreaseAction extends AbstractAction {
    private Object value;

    public IncreaseAction(String property, String contextEntity, String contextValue) {
        super(ActionType.INCREASE, property, contextEntity, contextValue);
    }

    @Override
    public void updateValue(UpdateObject updateObject) {
        OneObjectUpdate oneObjectUpdate = (OneObjectUpdate)updateObject;
        value = oneObjectUpdate.getObjectForUpdate();
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {
        Property toIncrease = entityInstance.getPropertyByName(getContextProperty());
        switch (toIncrease.getType())
        {
            case DECIMAL:
                toIncrease.setValue((int)toIncrease.getValue() + (int)value);
                break;
            case FLOAT:
                toIncrease.setValue((double)toIncrease.getValue() + (double)value);
                break;
        }
    }
}
