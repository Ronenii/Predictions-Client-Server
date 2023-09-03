package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;

import java.io.Serializable;

public abstract class OneEntAction extends AbstractAction implements Action, Serializable {


    public OneEntAction(ActionType type, String contextProperty, String contextEntity, SecondaryEntity secondaryEntity) {
        super(type, contextProperty, contextEntity, secondaryEntity);
    }

    abstract public void Invoke(EntityInstance entityInstance, int lastChangeTickCount);
}
