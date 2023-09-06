package simulation.properties.action.expression.impl;

import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.PropertyType;

public class RegularValueExpression extends AbstractExpression {

    private final Object value;

    public RegularValueExpression(PropertyType returnValueType, Object value) {
        super(returnValueType);
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return null;
    }

    @Override
    public Object evaluate() {
        return value;
    }
}
