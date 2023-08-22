package simulation.properties.action.expression.api;

/**
 * The Expression interface can hold 3 types of action's expression values: a method, property value or regular object.
 * The method 'evaluate' return the expression's value.
 */

public interface Expression {

    Object evaluate();
}
