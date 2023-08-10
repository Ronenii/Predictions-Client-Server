package properties.action.impl;

import objects.entity.Entity;
import properties.action.api.AbstractAction;
import properties.action.api.ActionType;
import properties.property.api.Property;

public class KillAction extends AbstractAction {
    public KillAction(String property, String contextEntity) {
        super(ActionType.KILL, property, contextEntity);
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
