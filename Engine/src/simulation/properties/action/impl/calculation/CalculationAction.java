package simulation.properties.action.impl.calculation;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class CalculationAction extends OneEntAction implements Serializable {
    private Expression arg1;
    private Expression arg2;
    private final CalculationType calculationType;

    public CalculationAction(String property, String contextEntity, SecondaryEntity secondaryEntity,Expression arg1, Expression arg2, CalculationType type1) {
        super(ActionType.CALCULATION, property, contextEntity, secondaryEntity);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.calculationType = type1;
    }

    public Object getArg1() {
        return arg1.evaluate();
    }

    public Object getArg2() {
        return arg2.evaluate();
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        Property toSet = entityInstance.getPropertyByName(getContextProperty());

        if(toSet == null){
            return;
        }

        switch (calculationType){
            case MULTIPLY:
                multiplyAndSetPropertyValue(toSet, lastChangeTickCount);
                break;
            case DIVIDE:
                divideAndSetPropertyValue(toSet, lastChangeTickCount);
                break;
        }
    }

    private void multiplyAndSetPropertyValue(Property toSet, int lastChangTickCount){
        switch (toSet.getType()){
            case DECIMAL:
                toSet.setValue((int)arg1.evaluate() * (int)arg2.evaluate(), lastChangTickCount);
                break;
            case FLOAT:
                toSet.setValue((double)arg1.evaluate() * (double)arg2.evaluate(), lastChangTickCount);
                break;
        }
    }
    private void divideAndSetPropertyValue(Property toSet, int lastChangTickCount){
        switch (toSet.getType()){
            case DECIMAL:
                toSet.setValue((int)arg1.evaluate() / (int)arg2.evaluate(), lastChangTickCount);
                break;
            case FLOAT:
                toSet.setValue((double)arg1.evaluate() / (double)arg2.evaluate(), lastChangTickCount);
                break;
        }
    }
}
