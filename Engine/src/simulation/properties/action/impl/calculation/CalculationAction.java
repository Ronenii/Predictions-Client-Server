package simulation.properties.action.impl.calculation;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class CalculationAction extends AbstractAction {
    private final String arg1;
    private final String arg2;

    private final ClaculationType type;

    public CalculationAction(String property, String contextEntity, String arg1, String arg2, ClaculationType type1) {
        super(ActionType.CALCULATION, property, contextEntity);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type1;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
