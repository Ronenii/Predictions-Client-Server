package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;

import java.io.Serializable;

public class KillAction extends OneEntAction implements Serializable {
    public KillAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity) {
        super(type, contextProperty, contextEntity, secondaryEntity);
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
