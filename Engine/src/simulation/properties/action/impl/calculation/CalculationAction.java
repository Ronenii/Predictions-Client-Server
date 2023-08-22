package simulation.properties.action.impl.calculation;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class CalculationAction extends AbstractAction implements Serializable {
    private Expression arg1;
    private Expression arg2;
    private final CalculationType type;

    public CalculationAction(String property, String contextEntity, Expression arg1, Expression arg2, CalculationType type1) {
        super(ActionType.CALCULATION, property, contextEntity);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type1;
    }

    public Object getArg1() {
        return arg1.evaluate();
    }

    public Object getArg2() {
        return arg2.evaluate();
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
                toSet.setValue((int)arg1.evaluate() * (int)arg2.evaluate());
                break;
            case FLOAT:
                toSet.setValue((double)arg1.evaluate() * (double)arg2.evaluate());
                break;
        }
    }
    private void divideAndSetPropertyValue(Property toSet){
        switch (toSet.getType()){
            case DECIMAL:
                toSet.setValue((int)arg1.evaluate() / (int)arg2.evaluate());
                break;
            case FLOAT:
                toSet.setValue((double)arg1.evaluate() / (double)arg2.evaluate());
                break;
        }
    }
}
