package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class SingleCondition extends AbstractConditionAction implements Serializable {
    private final ConditionOperator operator;

    public SingleCondition(String property, String contextEntity,SecondaryEntity secondaryEntity, ThenOrElse thenActions, ThenOrElse elseActions, ConditionOperator operator, Expression value) {
        super(property, contextEntity,secondaryEntity, thenActions, elseActions, value);
        this.operator = operator;
    }

    /**
     * This ctor is designated for a pure condition action without any then or else actions.
     * Mainly for the condition inside the secondary entity.
     */
    public SingleCondition(String property, String contextEntity, ConditionOperator operator, Expression value) {
        super(property, contextEntity, null, null, null, value);
        this.operator = operator;
    }

    @Override
    public void invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        Property toCompare = entityInstance.getPropertyByName(getContextProperty());

        if (toCompare == null) {
            return;
        }

        switch (operator) {
            // These 2 operators don;t require casting for comparison
            case EQUALS:
                if (toCompare.getValue() == getValue()) {
                    invokeThenActions(entityInstance, lastChangeTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangeTickCount);
                }
                break;
            case NOT_EQUALS:
                if (toCompare.getValue() != getValue()) {
                    invokeThenActions(entityInstance, lastChangeTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangeTickCount);
                }
                break;
                // For Lt & Bt we do require casting, it is handled in these funcs.
                // We don't use generics since it doesn't allow these operators.
            default:
                switch (toCompare.getType()){
                    case DECIMAL:
                        compareInequalityByInteger(toCompare, entityInstance, lastChangeTickCount);
                        break;
                    case FLOAT:
                        compareInequalityByFloat(toCompare, entityInstance, lastChangeTickCount);
                        break;
                }
        }
    }

    private void compareInequalityByInteger(Property toCompare, EntityInstance entityInstance, int lastChangTickCount){
        switch (operator) {
            case BIGGER_THAN:
                if ((int) toCompare.getValue() > (int) getValue()) {
                    invokeThenActions(entityInstance, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangTickCount);
                }
                break;
            case LESSER_THAN:
                if ((int) toCompare.getValue() < (int) getValue()) {
                    invokeThenActions(entityInstance, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangTickCount);
                }
                break;
        }
    }

    private void compareInequalityByFloat(Property toCompare, EntityInstance entityInstance, int lastChangTickCount){
        switch (operator) {
            case BIGGER_THAN:
                if ((double) toCompare.getValue() > (double) getValue()) {
                    invokeThenActions(entityInstance, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangTickCount);
                }
                break;
            case LESSER_THAN:
                if ((double) toCompare.getValue() < (double) getValue()) {
                    invokeThenActions(entityInstance, lastChangTickCount);
                } else {
                    invokeElseActions(entityInstance, lastChangTickCount);
                }
                break;
        }
    }
}
