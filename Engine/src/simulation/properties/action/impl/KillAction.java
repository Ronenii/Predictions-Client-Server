package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;

import java.io.Serializable;

public class KillAction extends OneEntAction implements Serializable {
    public KillAction(String property, String contextEntity, SecondaryEntity secondaryEntity) {
        super(ActionType.KILL, property,contextEntity, secondaryEntity);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        entityInstance.kill();
    }
}
