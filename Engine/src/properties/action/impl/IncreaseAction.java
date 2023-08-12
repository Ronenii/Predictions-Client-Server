package properties.action.impl;

import objects.entity.Entity;
import properties.action.api.AbstractAction;
import properties.action.api.ActionType;
import properties.property.api.Property;

public class IncreaseAction extends AbstractAction {
    private Object byExpression;

    public IncreaseAction(String property, String contextEntity, Object byExpression) {
        super(ActionType.INCREASE, property, contextEntity);
        this.byExpression = byExpression;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
