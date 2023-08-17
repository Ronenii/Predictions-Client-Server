package simulation.properties.action.impl.calculation;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.TwoObjectUpdate;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class CalculationAction extends AbstractAction {
    private Object arg1;
    private Object arg2;
    private final ClaculationType type;

    public CalculationAction(String property, String contextEntity, Object arg1, Object arg2, ClaculationType type1, String contextValue) {
        super(ActionType.CALCULATION, property, contextEntity, contextValue);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type1;
    }

    @Override
    public void updateValue(UpdateObject updateObject) {
        TwoObjectUpdate twoObjectUpdate = (TwoObjectUpdate)updateObject;
        arg1 = twoObjectUpdate.getObjectForUpdate1();
        arg2 = twoObjectUpdate.getGetObjectForUpdate2();
    }

    public Object getArg1() {
        return arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
