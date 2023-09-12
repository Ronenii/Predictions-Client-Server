package simulation.properties.action.impl.condition;


import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;

import java.io.Serializable;
import java.util.List;

public class MultipleCondition extends AbstractConditionAction implements Serializable {
    private final ConditionOperator logical;

    private final List<AbstractConditionAction> subConditions;

    public MultipleCondition(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity, Expression value, ThenOrElse thenActions, ThenOrElse elseActions, ConditionOperator logical, List<AbstractConditionAction> subConditions) {
        super(type, contextProperty, contextEntity, secondaryEntity, value, thenActions, elseActions);
        this.logical = logical;
        this.subConditions = subConditions;
    }

    public MultipleCondition(ActionType type, Expression property, String contextEntity, ConditionOperator logical, List<AbstractConditionAction> conditions, Expression value) {
        super(type, property, contextEntity, null, value, null, null);
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
    public void invoke(EntityInstance entityInstance, Grid grid, int lastChangeTickCount) {
        boolean isFirst = true;

        for (AbstractConditionAction a : subConditions) {
            a.invoke(entityInstance, grid, lastChangeTickCount);
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
            invokeThenActions(entityInstance, grid, lastChangeTickCount);
        }else{
            invokeElseActions(entityInstance, grid, lastChangeTickCount);
        }
    }

    @Override
    public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) {
        boolean isFirst = true;

        for (AbstractConditionAction a : subConditions) {
            if(a.getContextEntity().equals(primaryInstance.getInstanceEntityName())) {
                a.invoke(primaryInstance, grid, lastChangeTickCount);
            }
            else {
                a.invoke(secondaryInstance,grid, lastChangeTickCount);
            }

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
            invokeThenActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
        }else{
            invokeElseActionsWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
        }
    }
}
