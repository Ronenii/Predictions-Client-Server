package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.properties.property.api.Property;

import java.io.Serializable;

public class SingleCondition extends AbstractConditionAction implements Serializable {
    private final ConditionOperator operator;

    public SingleCondition(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, ConditionOperator operator, String contextValue) {
        super(property, contextEntity, thenActions, elseActions, contextValue);
        this.operator = operator;
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {
        Property toCompare = entityInstance.getPropertyByName(getContextProperty());

        if (toCompare == null) {
            return;
        }

        switch (operator) {
            // These 2 operators don;t require casting for comparison
            case EQUALS:
                if (toCompare.getValue() == getValue()) {
                    invokeThenActions(entityInstance);
                } else {
                    invokeElseActions(entityInstance);
                }
                break;
            case NOT_EQUALS:
                if (toCompare.getValue() != getValue()) {
                    invokeThenActions(entityInstance);
                } else {
                    invokeElseActions(entityInstance);
                }
                break;
                // For Lt & Bt we do require casting, it is handled in these funcs.
                // We don't use generics since it doesn't allow these operators.
            default:
                switch (toCompare.getType()){
                    case DECIMAL:
                        compareInequalityByInteger(toCompare, entityInstance);
                        break;
                    case FLOAT:
                        compareInequalityByFloat(toCompare, entityInstance);
                        break;
                }
        }
    }

    private void compareInequalityByInteger(Property toCompare, EntityInstance entityInstance){
        switch (operator) {
            case BIGGER_THAN:
                if ((int) toCompare.getValue() > (int) getValue()) {
                    invokeThenActions(entityInstance);
                } else {
                    invokeElseActions(entityInstance);
                }
                break;
            case LESSER_THAN:
                if ((int) toCompare.getValue() < (int) getValue()) {
                    invokeThenActions(entityInstance);
                } else {
                    invokeElseActions(entityInstance);
                }
                break;
        }
    }

    private void compareInequalityByFloat(Property toCompare, EntityInstance entityInstance){
        switch (operator) {
            case BIGGER_THAN:
                if ((double) toCompare.getValue() > (double) getValue()) {
                    invokeThenActions(entityInstance);
                } else {
                    invokeElseActions(entityInstance);
                }
                break;
            case LESSER_THAN:
                if ((double) toCompare.getValue() < (double) getValue()) {
                    invokeThenActions(entityInstance);
                } else {
                    invokeElseActions(entityInstance);
                }
                break;
        }
    }
}
