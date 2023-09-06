package simulation.properties.action.expression.impl.methods;

import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

public class EvaluateExpression extends AbstractExpression {
    private Property property;

    public EvaluateExpression(PropertyType returnValueType, Property property) {
        super(returnValueType);
        this.property = property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public PropertyType getType() {
        return property.getType();
    }

    @Override
    public Object evaluate() {
        return property.getValue();
    }
}
