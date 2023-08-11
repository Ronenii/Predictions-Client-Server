package simulation.properties.action.impl;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class IncreaseAction extends AbstractAction {
    private String byExpression;

    public IncreaseAction(String property, String contextEntity, String byExpression) {
        super(ActionType.INCREASE, property, contextEntity);
        this.byExpression = byExpression;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
