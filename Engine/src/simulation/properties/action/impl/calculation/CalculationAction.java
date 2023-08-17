package simulation.properties.action.impl.calculation;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class CalculationAction extends AbstractAction {
    private final Object arg1;
    private final Object arg2;
    private final ClaculationType type;

    public CalculationAction(String property, String contextEntity, Object arg1, Object arg2, ClaculationType type1, String contextValue) {
        super(ActionType.CALCULATION, property, contextEntity, contextValue);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type1;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
