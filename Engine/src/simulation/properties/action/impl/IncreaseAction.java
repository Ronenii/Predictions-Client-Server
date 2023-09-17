package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.exception.CrashException;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.methods.PercentExpression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class IncreaseAction extends OneEntAction implements Serializable {
    private final Expression value;

    public IncreaseAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity, Expression value) {
        super(type, contextProperty, contextEntity, secondaryEntity);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Expression getValueExpression() {
        return value;
    }

    /**
     * Increases the value of a property based on the property's type.
     * Increases actions can only take place on decimal or floats.
     * @param entityInstance The given entity to increase the value of the action's property.
     */
    @Override
    public void invoke(EntityInstance entityInstance, boolean isExpressionUpdated, int lastChangeTickCount) throws CrashException {
        String propertyName = getContextProperty().getPropertyName();
        Property toIncrease = entityInstance.getPropertyByName(propertyName);

        if(toIncrease == null){
            throw new CrashException(String.format("in action 'Increase', %s %s does not exists",entityInstance.getInstanceEntityName(), propertyName));
        }

        if (!isExpressionUpdated){
            updateExpression(entityInstance, value);
        }

        switch (toIncrease.getType())
        {
            case DECIMAL:
                toIncrease.setValue((int)toIncrease.getValue() + (int)value.evaluate(), lastChangeTickCount);
                break;
            case FLOAT:
                toIncrease.setValue((double)toIncrease.getValue() + (double)value.evaluate(), lastChangeTickCount);
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

        return new IncreaseAction(getType(), dupProperty, getContextEntity(), getSecondaryEntity(), dupValue);
    }
}
