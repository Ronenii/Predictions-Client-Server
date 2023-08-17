package simulation.properties.action.impl;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class DecreaseAction extends AbstractAction {
    private Object byExpression;

    public DecreaseAction(String property, String contextEntity, String contextValue) {
        super(ActionType.DECREASE, property, contextEntity, contextValue);
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
