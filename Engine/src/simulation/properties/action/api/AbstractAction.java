package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.PropertyValueExpression;
import simulation.properties.action.expression.impl.RegularValueExpression;
import simulation.properties.action.expression.impl.methods.EvaluateExpression;
import simulation.properties.action.expression.impl.methods.PercentExpression;
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
        private final int count; // If count value is -1 -> Got 'All' from the PRDSelection.
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

    // Todo: find where to use this.
    protected void updatePercentExpression(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, PercentExpression percentExpression) {
        Expression exp1 = percentExpression.getArg1(), exp2 = percentExpression.getArg2();

        if (exp1 instanceof PercentExpression){
            updatePercentExpression(firstEntityInstance, secondEntityInstance, (PercentExpression)exp1);
        }
        else {
            updateSubExpression(firstEntityInstance, secondEntityInstance, exp1);
        }

        if(exp2 instanceof PercentExpression){
            updatePercentExpression(firstEntityInstance, secondEntityInstance, (PercentExpression)exp2);
        }
        else {
            updateSubExpression(firstEntityInstance, secondEntityInstance, exp2);
        }
    }

    private void updateSubExpression(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, Expression expression){
        String expressionEntityName = getEntityNameIfEvalOrTicksOrRegularProperty(expression);

        if(expressionEntityName != null){
            if(expressionEntityName.equals(firstEntityInstance.getInstanceEntityName())){
                updateExpression(firstEntityInstance, expression);
            }
            else {
                updateExpression(secondEntityInstance, expression);
            }
        }
    }

    private String getEntityNameIfEvalOrTicksOrRegularProperty(Expression expression) {
        String expressionEntityName = null;

        if(expression instanceof EvaluateExpression){
            expressionEntityName = ((EvaluateExpression)expression).getEntityName();
        } else if (expression instanceof TicksExpression) {
            expressionEntityName = ((TicksExpression)expression).getEntityName();
        } else if (expression instanceof PropertyValueExpression) {
            expressionEntityName = ((PropertyValueExpression)expression).getPropertyEntityName();
        }

        return expressionEntityName;
    }

}
