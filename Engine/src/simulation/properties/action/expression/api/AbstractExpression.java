package simulation.properties.action.expression.api;

import simulation.properties.property.api.PropertyType;


public abstract class AbstractExpression implements Expression {
    private final PropertyType returnValueType;

    public AbstractExpression(PropertyType returnValueType) {
        this.returnValueType = returnValueType;
    }

    public PropertyType getReturnValueType() {
        return returnValueType;
    }
}
