package simulation.properties.action.impl;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

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
