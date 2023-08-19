package simulation.properties.action.impl;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

import java.io.Serializable;

public class KillAction extends AbstractAction implements Serializable {
    public KillAction(String property, String contextEntity, String contextValue) {
        super(ActionType.KILL, property, contextEntity, contextValue);
    }

    @Override
    public void updateValue(UpdateObject updateObject) {
        //TODO: find what to do with that shit.
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {
        entityInstance.kill();
    }
}
