package simulation.properties.action.impl;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class SetAction extends AbstractAction implements Serializable {
    private Expression value;

    public SetAction(String property, String contextEntity, Expression value) {
        super(ActionType.SET, property, contextEntity);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value.evaluate();
    }
    @Override
    public void Invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        Property toSet = entityInstance.getPropertyByName(getContextProperty());
        if(toSet == null){
            return;
        }

        toSet.setValue(value.evaluate(), lastChangeTickCount);
    }
}
