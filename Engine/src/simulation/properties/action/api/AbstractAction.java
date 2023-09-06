package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.PropertyValueExpression;
import simulation.properties.action.expression.impl.RegularValueExpression;
import simulation.properties.action.expression.impl.methods.EvaluateExpression;
import simulation.properties.action.expression.impl.methods.TicksExpression;
import simulation.properties.property.api.Property;

public abstract class AbstractAction implements Action {

    private final ActionType type;
    protected final Expression contextProperty;
    private final String contextEntity;

    private final SecondaryEntity secondaryEntity;

    public AbstractAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity) {
        this.type = type;
        this.contextEntity = contextEntity;
        this.contextProperty = contextProperty;
        this.secondaryEntity = secondaryEntity;
    }

    @Override
    public ActionType getType() {
        return type;
    }

    @Override
    public Expression getContextProperty() {
        return contextProperty;
    }

    @Override
    public String getContextEntity() {
        return contextEntity;
    }

    @Override
    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }

    public static class SecondaryEntity{
        protected final String contextEntity;
        private final int count;
        private final Action Condition;

        public SecondaryEntity(String contextEntity, int count, Action condition) {
            this.contextEntity = contextEntity;
            this.count = count;
            Condition = condition;
        }

        public String getContextEntity() {
            return contextEntity;
        }

        public int getCount() {
            return count;
        }

        public Action getCondition() {
            return Condition;
        }
    }
    
    protected void updateExpression(EntityInstance entityInstance, Expression expression){
        if(!(expression instanceof RegularValueExpression)){
            String propertyName = ((Property)getContextProperty().evaluate()).getName();
            Property instanceProperty = entityInstance.getPropertyByName(propertyName);

            if(expression instanceof PropertyValueExpression) {
                ((PropertyValueExpression)expression).setProperty(instanceProperty);
            } else if (expression instanceof TicksExpression) {
                ((TicksExpression)expression).setProperty(instanceProperty);
            } else if (expression instanceof EvaluateExpression) {
                ((EvaluateExpression)expression).setProperty(instanceProperty);
            }
        }
    }

}
