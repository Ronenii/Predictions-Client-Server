package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
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
    public void invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        String propertyName = ((Property)getContextProperty().evaluate()).getName();
        Property toIncrease = entityInstance.getPropertyByName(propertyName);

        if(toIncrease == null){
            return;
        }

        updateExpression(entityInstance, value);
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
}
