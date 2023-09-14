package simulation.properties.action.expression.impl;

import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.PropertyType;

public class RegularValueExpression extends AbstractExpression {

    private final Object value;
    private final PropertyType type;

    public RegularValueExpression(PropertyType returnValueType, Object value, PropertyType type) {
        super(returnValueType);
        this.value = value;
        this.type = type;
    }

    @Override
    public PropertyType getType() {
        return type;
    }

    @Override
    public Object evaluate() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public Expression dupExpression() {
        return new RegularValueExpression(getReturnValueType(), value, type);
    }
}
