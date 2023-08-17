package simulation.properties.action.impl;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class IncreaseAction extends AbstractAction {
    private Object byExpression;

    public IncreaseAction(String property, String contextEntity, String contextValue) {
        super(ActionType.INCREASE, property, contextEntity, contextValue);
    }

    @Override
    public void updateValue(UpdateObject updateObject) {
        OneObjectUpdate oneObjectUpdate = (OneObjectUpdate)updateObject;
        byExpression = oneObjectUpdate.getObjectForUpdate();
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}