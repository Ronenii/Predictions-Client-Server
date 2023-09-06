package simulation.properties.action.expression.api;

import simulation.properties.property.api.PropertyType;

/**
 * The Expression interface can hold 3 types of action's expression values: a method, property value or regular object.
 * The method 'evaluate' return the expression's value.
 */

public interface Expression {

    Object evaluate();

    PropertyType getType();
}
