package simulation.properties.action.impl.calculation;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.methods.PercentExpression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class CalculationAction extends AbstractAction implements Serializable {
    private final Expression arg1;
    private final Expression arg2;
    private final CalculationType type;

    public CalculationAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity, Expression arg1, Expression arg2, CalculationType type1) {
        super(type, contextProperty, contextEntity, secondaryEntity);
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type1;
    }

    public Object getArg1() {
        return arg1.evaluate();
    }

    public Expression getArg1Expression(){return arg1;}

    public Expression getArg2Expression(){return arg2;}

    public Object getArg2() {
        return arg2.evaluate();
    }

    public CalculationType getCalculationType() {
        return type;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Expression getValueExpression() {
        return null;
    }

    public void invoke(EntityInstance entityInstance, boolean isArg1Updated, boolean isArg2Updated, int lastChangeTickCount) {
        String propertyName = getContextProperty().getPropertyName();
        Property toSet = entityInstance.getPropertyByName(propertyName);

        if(toSet == null){
            return;
        }

        if(!isArg1Updated) {
            updateExpression(entityInstance, arg1);
        }

        if(!isArg2Updated) {
            updateExpression(entityInstance, arg2);
        }

        switch (type){
            case MULTIPLY:
                multiplyAndSetPropertyValue(toSet, lastChangeTickCount);
                break;
            case DIVIDE:
                divideAndSetPropertyValue(toSet, lastChangeTickCount);
                break;
        }
    }

    public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, int lastChangeTickCount) {
        EntityInstance instanceForInvoke;
        boolean isArg1Updated, isArg2Updated;

        if(getContextEntity().equals(primaryInstance.getInstanceEntityName())){
            instanceForInvoke = primaryInstance;
        }
        else {
            instanceForInvoke = secondaryInstance;
        }

        isArg1Updated = updateExpressionWithSecondary(primaryInstance, secondaryInstance, arg1);
        isArg2Updated = updateExpressionWithSecondary(primaryInstance, secondaryInstance, arg2);
        invoke(instanceForInvoke, isArg1Updated, isArg2Updated, lastChangeTickCount);
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

    @Override
    public Action dupAction() {
        Expression dupProperty = null, dupArg1 = null, dupArg2 = null;

        if(getContextProperty() != null) {
            dupProperty = getContextProperty().dupExpression();
        }

        if (arg1 != null) {
            dupArg1 = arg1.dupExpression();
        }

        if (arg2 != null) {
            dupArg2 = arg2.dupExpression();
        }

        return new CalculationAction(getType(), dupProperty, getContextEntity(), getSecondaryEntity(), dupArg1, dupArg2, type);
    }
}
