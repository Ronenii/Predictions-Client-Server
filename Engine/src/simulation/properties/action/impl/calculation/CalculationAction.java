package simulation.properties.action.impl.calculation;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.TwoObjectUpdate;
import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.property.api.Property;

public class CalculationAction extends AbstractAction {
    private Object arg1;
    private Object arg2;
    private final CalculationType type;

    public CalculationAction(String property, String contextEntity, Object arg1, Object arg2, CalculationType type1, String contextValue) {
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
    public Object getValue() {
        return null;
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {
        Property toSet = entityInstance.getPropertyByName(getContextProperty());

        if(toSet == null){
            return;
        }

        switch (type){
            case MULTIPLY:
                multiplyAndSetPropertyValue(toSet);
                break;
            case DIVIDE:
                divideAndSetPropertyValue(toSet);
                break;
        }
    }

    private void multiplyAndSetPropertyValue(Property toSet){
        switch (toSet.getType()){
            case DECIMAL:
                toSet.setValue((int)arg1 * (int)arg2);
                break;
            case FLOAT:
                toSet.setValue((double)arg1 * (double)arg2);
                break;
        }
    }
    private void divideAndSetPropertyValue(Property toSet){
        switch (toSet.getType()){
            case DECIMAL:
                toSet.setValue((int)arg1 / (int)arg2);
                break;
            case FLOAT:
                toSet.setValue((double)arg1 / (double)arg2);
                break;
        }
    }
}
