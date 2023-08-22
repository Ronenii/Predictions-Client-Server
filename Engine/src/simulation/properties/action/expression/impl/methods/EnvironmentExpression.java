package simulation.properties.action.expression.impl.methods;

import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

public class EnvironmentExpression extends AbstractExpression {

    private final Property envProperty;

    public EnvironmentExpression(PropertyType returnValueType, Property envProperty) {
        super(returnValueType);
        this.envProperty = envProperty;
    }

    @Override
    public Object evaluate() {
        return envProperty.getValue();
    }
}
