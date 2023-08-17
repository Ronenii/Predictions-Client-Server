package simulation.properties.action.impl;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.property.api.Property;

public class DecreaseAction extends AbstractAction {
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
    public void Invoke(EntityInstance entityInstance) {
        Property toDecrease = entityInstance.getPropertyByName(getContextProperty());
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
