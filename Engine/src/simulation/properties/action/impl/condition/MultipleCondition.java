package simulation.properties.action.impl.condition;


import simulation.objects.entity.EntityInstance;

import java.util.List;

public class MultipleCondition extends AbstractConditionAction {
    private final ConditionOperator logical;

    private final List<AbstractConditionAction> subConditions;

    public MultipleCondition(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, ConditionOperator logical, List<AbstractConditionAction> conditions, String contextValue) {
        super(property, contextEntity, thenActions, elseActions, contextValue);
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
    public void Invoke(EntityInstance entityInstance) {
        boolean isFirst = true;
        for (AbstractConditionAction a : subConditions
        ) {
            a.Invoke(entityInstance);
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
            invokeThenActions(entityInstance);
        }else{
            invokeElseActions(entityInstance);
        }
    }
}
