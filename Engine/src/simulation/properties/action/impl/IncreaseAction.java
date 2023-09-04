package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class IncreaseAction extends OneEntAction implements Serializable {
    private final Expression value;

    public IncreaseAction(String property, String contextEntity, SecondaryEntity secondaryEntity, Expression value) {
        super(ActionType.INCREASE, property, contextEntity,secondaryEntity);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Increases the value of a property based on the property's type.
     * Increases actions can only take place on decimal or floats.
     * @param entityInstance The given entity to increase the value of the action's property.
     */
    @Override
    public void invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        Property toIncrease = entityInstance.getPropertyByName(getContextProperty());

        if(toIncrease == null){
            return;
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
}
