package simulation.properties.action.impl;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class SetAction extends AbstractAction {
    private Object value;

    public SetAction(String property, String contextEntity, String contextValue) {
        super(ActionType.SET, property, contextEntity, contextValue);
    }

    public void updateValue(Object value){
        this.value = value;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
