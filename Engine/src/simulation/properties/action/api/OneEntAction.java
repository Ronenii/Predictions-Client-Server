package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.expression.api.Expression;

import java.io.Serializable;

public abstract class OneEntAction extends AbstractAction implements Action, Serializable {


    public OneEntAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity) {
        super(type, contextProperty, contextEntity, secondaryEntity);
    }

    abstract public void invoke(EntityInstance entityInstance, int lastChangeTickCount);

}
