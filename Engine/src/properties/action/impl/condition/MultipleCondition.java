package properties.action.impl.condition;

import objects.Entity;
import properties.property.api.Property;

public class MultipleCondition extends AbstractConditionAction{
    public MultipleCondition(Property property, Entity contextEntity, String operator, String value) {
        super(property, contextEntity, operator, value);
    }

    @Override
    public void Invoke() {

    }
}
