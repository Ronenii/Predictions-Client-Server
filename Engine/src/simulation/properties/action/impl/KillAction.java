package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.Action;
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
    public Expression getValueExpression() {
        return null;
    }

    @Override
    public void invoke(EntityInstance entityInstance, boolean isExpressionUpdated, int lastChangeTickCount) {
        entityInstance.kill();
    }

    @Override
    public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, int lastChangeTickCount) {
        EntityInstance instanceForInvoke;

        if(getContextEntity().equals(primaryInstance.getInstanceEntityName())){
            instanceForInvoke = primaryInstance;
        }
        else {
            instanceForInvoke = secondaryInstance;
        }

        invoke(instanceForInvoke, true, lastChangeTickCount);
    }

    @Override
    public Action dupAction() {
        Expression dupProperty = null;

        if(getContextEntity() != null) {
            dupProperty = getContextProperty().dupExpression();
        }

        return new KillAction(getType(), dupProperty, getContextEntity(), getSecondaryEntity());
    }
}

