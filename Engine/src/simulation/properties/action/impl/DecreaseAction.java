package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class DecreaseAction extends AbstractAction implements Serializable {
    private final Expression value;

    public DecreaseAction(String property, String contextEntity, Expression value) {
        super(ActionType.DECREASE, property, contextEntity);
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
    public void Invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        Property toDecrease = entityInstance.getPropertyByName(getContextProperty());

        if(toDecrease == null){
            return;
        }

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
