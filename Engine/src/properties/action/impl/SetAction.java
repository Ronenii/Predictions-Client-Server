package properties.action.impl;

import objects.Entity;
import properties.action.api.AbstractAction;
import properties.action.api.ActionType;
import properties.property.api.Property;

public class SetAction extends AbstractAction {
    private final String value;

    public SetAction(Property property, Entity contextEntity, String value) {
        super(ActionType.SET, property, contextEntity);
        this.value = value;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
