package properties.action.impl.condition;

import objects.Entity;
import properties.action.api.AbstractAction;
import properties.action.api.ActionType;
import properties.property.api.Property;


public abstract class AbstractConditionAction extends AbstractAction {
    private final String operator;
    private final String value;

    public AbstractConditionAction(Property property, Entity contextEntity, String operator, String value) {
        super(ActionType.CONDITION, property, contextEntity);
        this.operator = operator;
        this.value = value;
    }
}
