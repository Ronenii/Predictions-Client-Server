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

public class SetAction extends OneEntAction implements Serializable {
    private final Expression value;

    public SetAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity, Expression value) {
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

    @Override
    public void invoke(EntityInstance entityInstance, boolean isExpressionUpdated, int lastChangeTickCount) throws CrashException {
        String propertyName = getContextProperty().getPropertyName();
        Property toSet = entityInstance.getPropertyByName(propertyName);
        if(toSet == null){
            throw new CrashException(String.format("in action 'Decrease', %s %s does not exists",entityInstance.getInstanceEntityName(), propertyName));
        }

        if (!isExpressionUpdated){
            updateExpression(entityInstance, value);
        }

        toSet.setValue(value.evaluate(), lastChangeTickCount);
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

        return new SetAction(getType(), dupProperty, getContextEntity(), getSecondaryEntity(), dupValue);
    }
}
