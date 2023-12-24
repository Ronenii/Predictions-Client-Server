package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.exception.CrashException;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.methods.RandomExpression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class DecreaseAction extends OneEntAction implements Serializable {
    private final Expression value;

    public DecreaseAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity, Expression value) {
        super(type, contextProperty, contextEntity, secondaryEntity);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value.evaluate();
    }

    @Override
    public Expression getValueExpression() {
        return value;
    }

    /**
     * Decreases the value of a property based on the property's type.
     * Decrease actions can only take place on decimal or floats.
     * @param entityInstance The given entity to decrease the value of the action's property from.
     */
    @Override
    public void invoke(EntityInstance entityInstance, boolean isExpressionUpdated, int lastChangeTickCount) throws CrashException {
        String propertyName = getContextProperty().getPropertyName();
        Property toDecrease = entityInstance.getPropertyByName(propertyName);

        if(toDecrease == null){
            throw new CrashException(String.format("in action 'Decrease', %s %s does not exists",entityInstance.getInstanceEntityName(), propertyName));
        }

        if (!isExpressionUpdated){
            updateExpression(entityInstance, value);
        }

        switch (toDecrease.getType())
        {
            case DECIMAL:
                toDecrease.setValue((int)toDecrease.getValue() - (int)value.evaluate(), lastChangeTickCount);
                break;
            case FLOAT:
                if(value instanceof RandomExpression){
                    double doubleValue = (int)value.evaluate();
                    toDecrease.setValue((double)toDecrease.getValue() - doubleValue, lastChangeTickCount);
                }else {
                    toDecrease.setValue((double)toDecrease.getValue() - (double)value.evaluate(), lastChangeTickCount);
                }

                break;
        }
    }

    @Override
    public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, int lastChangeTickCount) throws CrashException {
        EntityInstance instanceForInvoke;
        boolean isExpressionUpdated;

        if(getContextEntity().equals(primaryInstance.getInstanceEntityName())){
            instanceForInvoke = primaryInstance;
        }
        else {
            instanceForInvoke = secondaryInstance;
        }

        isExpressionUpdated = updateExpressionWithSecondary(primaryInstance, secondaryInstance, value);
        invoke(instanceForInvoke, isExpressionUpdated, lastChangeTickCount);
    }

    @Override
    public Action dupAction() {
        Expression dupProperty = null, dupValue = null;

        if(getContextProperty() != null) {
            dupProperty = getContextProperty().dupExpression();
        }

        if (value != null) {
            dupValue = value.dupExpression();
        }

        return new DecreaseAction(getType(), dupProperty, getContextEntity(), getSecondaryEntity(), dupValue);
    }
}
