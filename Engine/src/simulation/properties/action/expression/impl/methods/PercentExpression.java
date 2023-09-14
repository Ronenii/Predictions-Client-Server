package simulation.properties.action.expression.impl.methods;

import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.PropertyType;

public class PercentExpression extends AbstractExpression {

    private final Expression arg1;
    private final Expression arg2;

    public PercentExpression(PropertyType returnValueType, Expression arg1, Expression arg2) {
        super(returnValueType);
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.FLOAT;
    }

    public Expression getArg1() {
        return arg1;
    }

    public Expression getArg2() {
        return arg2;
    }

    @Override
    public String toString() {
        return String.format("Percent(%s,%s)", arg1.toString(), arg2.toString());
    }

    @Override
    public Object evaluate() {
        // ask aviad if the numbers must be int or double or both.
        Object ret;
        if (arg1.evaluate().getClass() == Integer.class) {
            int intArg1 = (int)arg1.evaluate(), intArg2 = (int)arg2.evaluate();
            ret = (intArg1 * intArg2) / 100;
        }
        else {
            double doubleArg1 = (double)arg1.evaluate(), doubleArg2 = (double)arg2.evaluate();
            ret = (doubleArg1 * doubleArg2) / 100.00;
        }

        return ret;
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public Expression dupExpression() {
        return new PercentExpression(getReturnValueType(), arg1.dupExpression(), arg2.dupExpression());
    }
}
