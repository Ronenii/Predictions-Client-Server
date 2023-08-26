package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

import java.io.Serializable;

public class KillAction extends AbstractAction implements Serializable {
    public KillAction(String property, String contextEntity) {
        super(ActionType.KILL, property, contextEntity);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void Invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        entityInstance.kill();
    }
}
