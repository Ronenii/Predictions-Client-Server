package simulation.properties.action.expression.impl;

import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

public class PropertyValueExpression extends AbstractExpression {
    private Property property;

    public PropertyValueExpression(PropertyType returnValueType, Property property) {
        super(returnValueType);
        this.property = property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getPropertyEntityName(){
        return property.getEntityName();
    }
    @Override
    public String getPropertyName(){return property.getName();}

    @Override
    public PropertyType getType() {
        return property.getType();
    }

    @Override
    public Object evaluate() {
        return property.getValue();
    }

    @Override
    public String toString() {
        return property.getName();
    }

    @Override
    public Expression dupExpression() {
        return new PropertyValueExpression(getReturnValueType(), property.dupProperty());
    }
}
