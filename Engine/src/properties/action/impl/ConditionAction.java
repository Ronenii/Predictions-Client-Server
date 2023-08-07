package properties.action.impl;

import objects.entity.Entity;
import properties.action.api.AbstractAction;
import properties.action.api.ActionType;
import properties.property.api.Property;

// TODO: split condition, make this class abstract and create two extenders classes: SingleCondition and MultipleCondition.
public class ConditionAction extends AbstractAction {
    private final String singularity;
    private final String operator;
    private final String value;

    public ConditionAction(Property property, Entity contextEntity, String singularity, String operator, String value) {
        super(ActionType.CONDITION, property, contextEntity);
        this.singularity = singularity;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public void Invoke() {
        // TODO: implementation.
    }
}
