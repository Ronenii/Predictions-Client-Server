package simulation.properties.action.impl.condition;


import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.expression.api.Expression;

import java.io.Serializable;
import java.util.List;

public class MultipleCondition extends AbstractConditionAction implements Serializable {
    private final ConditionOperator logical;

    private final List<AbstractConditionAction> subConditions;

    public MultipleCondition(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, ConditionOperator logical, List<AbstractConditionAction> conditions, Expression value, AbstractConditionAction.SecondaryEntity secondaryEntity) {
        super(property, contextEntity, thenActions, elseActions, value, secondaryEntity);
        this.logical = logical;
        this.subConditions = conditions;
    }

    public MultipleCondition(String property, String contextEntity, ConditionOperator logical, List<AbstractConditionAction> conditions, Expression value) {
        super(property, contextEntity, null, null, value, null);
        this.logical = logical;
        this.subConditions = conditions;
    }

    public ConditionOperator getLogical() {
        return logical;
    }

    public List<AbstractConditionAction> getSubConditions() {
        return subConditions;
    }


    /**
     * Iterates on all nested conditions and invokes each on the given entity.
     * If all nested conditions are true the invokes "thenActions" of this multiple condition,
     * on the given instance.
     * Else, invokes all "else actions".
     * @param entityInstance The given instance to invoke the condition action on.
     */
    @Override
    public void Invoke(EntityInstance entityInstance, int lastChangeTickCount) {
        boolean isFirst = true;
        for (AbstractConditionAction a : subConditions
        ) {
            a.Invoke(entityInstance, lastChangeTickCount);
            if(isFirst){
                isTrue = a.isTrue;
                isFirst = false;
            }
            else{
                switch (logical) {
                    case AND:
                        isTrue &= a.isTrue;
                        break;
                    case OR:
                        isTrue |= a.isTrue;
                        break;
                }

            }
        }
        if(isTrue){
            invokeThenActions(entityInstance, lastChangeTickCount);
        }else{
            invokeElseActions(entityInstance, lastChangeTickCount);
        }
    }
}
