package simulation.properties.action.expression.impl.methods;

import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.property.api.PropertyType;

import java.util.Random;

public class RandomExpression extends AbstractExpression {
    private final int range;

    public RandomExpression(PropertyType returnValueType, int range) {
        super(returnValueType);
        this.range = range;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.DECIMAL;
    }

    @Override
    public Object evaluate() {
        Random random = new Random();
        return random.nextInt(range + 1);
    }

    @Override
    public String toString() {
        return String.format("Random(%d)", range);
    }
}
