package simulation.properties.action.expression.impl.methods;

import simulation.objects.world.ticks.counter.TicksCounter;
import simulation.properties.action.expression.api.AbstractExpression;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

public class TicksExpression extends AbstractExpression {

    private Property property;
    private TicksCounter simulationTicks;
    private final String entityName;

    public TicksExpression(PropertyType returnValueType, Property property, TicksCounter simulationTicks, String entityName) {
        super(returnValueType);
        this.property = property;
        this.simulationTicks = simulationTicks;
        this.entityName = entityName;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setSimulationTicks(TicksCounter simulationTicks) {
        this.simulationTicks = simulationTicks;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.DECIMAL;
    }

    public String getEntityName() {
        return entityName;
    }
    @Override
    public String getPropertyName(){return property.getName();}

    @Override
    public String toString() {
        return String.format("Ticks(%s.%s)", entityName, property.getName());
    }

    @Override
    public Object evaluate() {
        return simulationTicks.getTicks() - property.getLastChangeTickCount();
    }

    @Override
    public Expression dupExpression() {
        return new TicksExpression(getReturnValueType(), property.dupProperty(),simulationTicks, entityName);
    }
}
