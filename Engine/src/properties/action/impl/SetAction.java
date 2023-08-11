package properties.action.impl;

import objects.entity.Entity;
import properties.action.api.AbstractAction;
import properties.action.api.ActionType;
import properties.property.api.Property;

public class SetAction extends AbstractAction {
    private final Object value;

    public SetAction(String property, String contextEntity, Object value) {
        super(ActionType.SET, property, contextEntity);
        this.value = value;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
