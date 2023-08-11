package simulation.properties.action.impl;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class KillAction extends AbstractAction {
    public KillAction(String property, String contextEntity) {
        super(ActionType.KILL, property, contextEntity);
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
