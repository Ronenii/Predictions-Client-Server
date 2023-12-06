package simulation.properties.action.expression.impl.methods;

import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

public class EnvironmentExpression extends AbstractExpression {

    private Property envProperty;

    public EnvironmentExpression(PropertyType returnValueType, Property envProperty) {
        super(returnValueType);
        this.envProperty = envProperty;
    }

    @Override
    public PropertyType getType() {
        return envProperty.getType();
    }

    @Override
    public String toString() {
        return String.format("Environment(%s)", envProperty.getName());
    }

    @Override
    public Object evaluate() {
        return envProperty.getValue();
    }

    public void setEnvProperty(Property envProperty) {
        this.envProperty = envProperty;
    }

    @Override
    public String getPropertyName() {
        return envProperty.getName();
    }

    @Override
    public Expression dupExpression() {
        return new EnvironmentExpression(getReturnValueType(), envProperty.dupProperty());
    }
}
