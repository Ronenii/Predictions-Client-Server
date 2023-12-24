package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.exception.CrashException;
import simulation.properties.action.expression.api.Expression;

import java.io.Serializable;

public abstract class OneEntAction extends AbstractAction implements Action, Serializable {
    public OneEntAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity) {
        super(type, contextProperty, contextEntity, secondaryEntity);
    }

    abstract public void invoke(EntityInstance entityInstance, boolean isExpressionUpdated, int lastChangeTickCount) throws CrashException;

    abstract public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, int lastChangeTickCount) throws CrashException;
}
