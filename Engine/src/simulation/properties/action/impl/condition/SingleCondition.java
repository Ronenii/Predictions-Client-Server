package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.PropertyValueExpression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class SingleCondition extends AbstractConditionAction implements Serializable {
    private final ConditionOperator operator;

    public SingleCondition(ActionType type, Expression property, String contextEntity, SecondaryEntity secondaryEntity, ThenOrElse thenActions, ThenOrElse elseActions, ConditionOperator operator, Expression value) {
        super(type, property, contextEntity,secondaryEntity, value, elseActions, thenActions);
        this.operator = operator;
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
    public void invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        Object valueToCompare;

        updateExpression(entityInstance, contextProperty);
        valueToCompare = contextProperty.evaluate();
        if (valueToCompare == null) {
            return;
        }

        updateExpression(entityInstance, value);
        switch (operator) {
            // These 2 operators don;t require casting for comparison
            case EQUALS:
                if (valueToCompare == getValue()) {
                    invokeThenActions(entityInstance, lastChangeTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangeTickCount);
                }
                break;
            case NOT_EQUALS:
                if (valueToCompare != getValue()) {
                    invokeThenActions(entityInstance, lastChangeTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangeTickCount);
                }
                break;
                // For Lt & Bt we do require casting, it is handled in these funcs.
                // We don't use generics since it doesn't allow these operators.
            default:
                switch (contextProperty.getType()){
                    case DECIMAL:
                        compareInequalityByInteger(valueToCompare, entityInstance, lastChangeTickCount);
                        break;
                    case FLOAT:
                        compareInequalityByFloat(valueToCompare, entityInstance, lastChangeTickCount);
                        break;
                }
        }
    }

    private void compareInequalityByInteger(Object toCompare, EntityInstance entityInstance, int lastChangTickCount){
        switch (operator) {
            case BIGGER_THAN:
                if ((int) toCompare > (int) getValue()) {
                    invokeThenActions(entityInstance, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangTickCount);
                }
                break;
            case LESSER_THAN:
                if ((int) toCompare < (int) getValue()) {
                    invokeThenActions(entityInstance, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangTickCount);
                }
                break;
        }
    }

    private void compareInequalityByFloat(Object toCompare, EntityInstance entityInstance, int lastChangTickCount){
        switch (operator) {
            case BIGGER_THAN:
                if ((double) toCompare > (double) getValue()) {
                    invokeThenActions(entityInstance, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangTickCount);
                }
                break;
            case LESSER_THAN:
                if ((double) toCompare < (double) getValue()) {
                    invokeThenActions(entityInstance, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangTickCount);
                }
                break;
        }
    }
}
