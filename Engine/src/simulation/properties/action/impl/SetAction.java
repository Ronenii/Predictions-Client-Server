package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class SetAction extends OneEntAction implements Serializable {
    private Expression value;

    public SetAction(String property, String contextEntity,SecondaryEntity secondaryEntity, Expression value) {
        super(ActionType.SET, property, contextEntity, secondaryEntity);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value.evaluate();
    }
    @Override
    public void invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        Property toSet = entityInstance.getPropertyByName(getContextProperty());
        if(toSet == null){
            return;
        }

        toSet.setValue(value.evaluate(), lastChangeTickCount);
    }
}
