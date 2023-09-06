package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
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

    /**
     * Decreases the value of a property based on the property's type.
     * Decrease actions can only take place on decimal or floats.
     * @param entityInstance The given entity to decrease the value of the action's property from.
     */
    @Override
    public void invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        String propertyName = ((Property)getContextProperty().evaluate()).getName();
        Property toDecrease = entityInstance.getPropertyByName(propertyName);

        if(toDecrease == null){
            return;
        }

        updateExpression(entityInstance, value);
        switch (toDecrease.getType())
        {
            case DECIMAL:
                toDecrease.setValue((int)toDecrease.getValue() - (int)value.evaluate(), lastChangeTickCount);
                break;
            case FLOAT:
                toDecrease.setValue((double)toDecrease.getValue() - (double)value.evaluate(), lastChangeTickCount);
                break;
        }
    }
}
