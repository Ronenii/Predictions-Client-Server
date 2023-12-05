package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.exception.CrashException;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.PropertyValueExpression;
import simulation.properties.action.expression.impl.methods.PercentExpression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class SingleCondition extends AbstractConditionAction implements Serializable {
    private final ConditionOperator operator;

    public SingleCondition(ActionType type, Expression property, String contextEntity, SecondaryEntity secondaryEntity, ThenOrElse thenActions, ThenOrElse elseActions, ConditionOperator operator, Expression value) {
        super(type, property, contextEntity,secondaryEntity, value, thenActions, elseActions);
        this.operator = operator;
    }

    public ConditionOperator getOperator() {
        return operator;
    }

    /**
     * This ctor is designated for a pure condition action without any then or else actions.
     * Mainly for the condition inside the secondary entity.
     */
    public SingleCondition(ActionType type, Expression property, String contextEntity, ConditionOperator operator, Expression value) {
        super(type, property, contextEntity, null, value, null, null);
        this.operator = operator;
    }

    @Override
    public void invoke(EntityInstance entityInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        Object valueToCompare;

        updateExpression(entityInstance, contextProperty);
        valueToCompare = contextProperty.evaluate();
        if (valueToCompare == null) {
            throw new CrashException("in action 'Single condition', the property value does not exists");
        }

        updateExpression(entityInstance, value);
        switch (operator) {
            // These 2 operators don;t require casting for comparison
            case EQUALS:
                if (valueToCompare == getValue()) {
                    invokeThenActions(entityInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActions(entityInstance, grid, lastChangeTickCount);
                }
                break;
            case NOT_EQUALS:
                if (valueToCompare != getValue()) {
                    invokeThenActions(entityInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActions(entityInstance, grid, lastChangeTickCount);
                }
                break;
                // For Lt & Bt we do require casting, it is handled in these funcs.
                // We don't use generics since it doesn't allow these operators.
            default:
                switch (contextProperty.getType()){
                    case DECIMAL:
                        compareInequalityByInteger(valueToCompare, entityInstance, grid, lastChangeTickCount);
                        break;
                    case FLOAT:
                        compareInequalityByFloat(valueToCompare, entityInstance, grid, lastChangeTickCount);
                        break;
                }
        }
    }

    private void compareInequalityByInteger(Object toCompare, EntityInstance entityInstance, Grid grid, int lastChangTickCount) throws CrashException {
        switch (operator) {
            case BIGGER_THAN:
                if ((int) toCompare > (int) getValue()) {
                    invokeThenActions(entityInstance, grid, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, grid, lastChangTickCount);
                }
                break;
            case LESSER_THAN:
                if ((int) toCompare < (int) getValue()) {
                    invokeThenActions(entityInstance, grid, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, grid, lastChangTickCount);
                }
                break;
        }
    }

    private void compareInequalityByIntegerWithSecondary(Object toCompare, EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        switch (operator) {
            case BIGGER_THAN:
                if ((int) toCompare > (int) getValue()) {
                    invokeThenActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                }
                break;
            case LESSER_THAN:
                if ((int) toCompare < (int) getValue()) {
                    invokeThenActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                }
                break;
        }
    }

    private void compareInequalityByFloat(Object toCompare, EntityInstance entityInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        switch (operator) {
            case BIGGER_THAN:
                if ((double) toCompare > (double) getValue()) {
                    invokeThenActions(entityInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActions(entityInstance, grid, lastChangeTickCount);
                }
                break;
            case LESSER_THAN:
                if ((double) toCompare < (double) getValue()) {
                    invokeThenActions(entityInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActions(entityInstance, grid, lastChangeTickCount);
                }
                break;
        }
    }

    private void compareInequalityByFloatWithSecondary(Object toCompare, EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        switch (operator) {
            case BIGGER_THAN:
                if ((double) toCompare > (double) getValue()) {
                    invokeThenActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                }
                break;
            case LESSER_THAN:
                if ((double) toCompare < (double) getValue()) {
                    invokeThenActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                }
                break;
        }
    }

    @Override
    public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        Object valueToCompare;
        // Try to update property with one of the entities.
        if(!updateExpressionWithSecondary(primaryInstance, secondaryInstance, contextProperty)){
            updateExpression(primaryInstance, contextProperty);
        }

        valueToCompare = contextProperty.evaluate();
        if (valueToCompare == null) {
            return;
        }
        // Try to update value with one of the entities.
        if(!updateExpressionWithSecondary(primaryInstance, secondaryInstance, value)) {
            updateExpression(primaryInstance, value);
        }

        switch (operator) {
            // These 2 operators don;t require casting for comparison
            case EQUALS:
                if (valueToCompare == getValue()) {
                    invokeThenActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                }
                break;
            case NOT_EQUALS:
                if (valueToCompare != getValue()) {
                    invokeThenActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                } else {
                    invokeElseActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                }
                break;
            // For Lt & Bt we do require casting, it is handled in these funcs.
            // We don't use generics since it doesn't allow these operators.
            default:
                switch (contextProperty.getType()){
                    case DECIMAL:
                        compareInequalityByIntegerWithSecondary(valueToCompare, primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                        break;
                    case FLOAT:
                        compareInequalityByFloatWithSecondary(valueToCompare, primaryInstance, secondaryInstance, grid, lastChangeTickCount);
                        break;
                }
        }
    }

    @Override
    public Action dupAction() {
        Expression dupProperty = null, dupValue = null;
        ThenOrElse thenDup = null, elseDup = null;

        if(getContextProperty() != null) {
            dupProperty = getContextProperty().dupExpression();
        }

        if (value != null) {
            dupValue = value.dupExpression();
        }

        if (thenActions != null) {
            thenDup = thenActions.dupThenOrElse();
        }

        if (elseActions != null) {
            elseDup = elseActions.dupThenOrElse();
        }

        return new SingleCondition(getType(), dupProperty, getContextEntity(), getSecondaryEntity(), thenDup, elseDup, operator, dupValue);
    }
}
