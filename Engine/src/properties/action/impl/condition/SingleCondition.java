package properties.action.impl.condition;

import objects.Entity;
import properties.property.api.Property;

public class SingleCondition extends AbstractConditionAction{
    public SingleCondition(Property property, Entity contextEntity, String operator, String value) {
        super(property, contextEntity, operator, value);
    }

    @Override
    public void Invoke() {

    }
}
