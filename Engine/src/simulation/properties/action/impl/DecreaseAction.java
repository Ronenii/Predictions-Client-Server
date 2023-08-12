package simulation.properties.action.impl;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class DecreaseAction extends AbstractAction {
    private Object byExpression;

    public DecreaseAction(String property, String contextEntity, Object byExpression) {
        super(ActionType.DECREASE, property, contextEntity);
        this.byExpression = byExpression;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
