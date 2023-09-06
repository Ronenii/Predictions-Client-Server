package simulation.properties.action.expression.impl.methods;

import simulation.objects.world.ticks.counter.TicksCounter;
import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

public class TicksExpression extends AbstractExpression {

    private Property property;
    private final TicksCounter simulationTicks;

    public TicksExpression(PropertyType returnValueType, Property property, TicksCounter simulationTicks) {
        super(returnValueType);
        this.property = property;
        this.simulationTicks = simulationTicks;
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
        return simulationTicks.getTicks() - property.getLastChangeTickCount();
    }
}
