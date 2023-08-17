package simulation.properties.action.impl;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class DecreaseAction extends AbstractAction {
    private Object byExpression;

    public DecreaseAction(String property, String contextEntity, String contextValue) {
        super(ActionType.DECREASE, property, contextEntity, contextValue);
    }

    public void updateValue(Object byExpression){
        this.byExpression = byExpression;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
