package simulation.properties.action.impl;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

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
