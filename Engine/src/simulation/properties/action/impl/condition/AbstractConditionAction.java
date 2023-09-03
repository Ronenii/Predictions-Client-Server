package simulation.properties.action.impl.condition;

import jaxb.schema.generated.PRDAction;
import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;

import java.io.Serializable;


public abstract class AbstractConditionAction extends OneEntAction implements Serializable {
    protected Expression value;
    protected boolean isTrue;
    protected final ThenOrElse thenActions;
    protected final ThenOrElse elseActions;
    protected final SecondaryEntity secondaryEntity;

    public AbstractConditionAction(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, Expression value, SecondaryEntity secondaryEntity) {
        super(ActionType.CONDITION, property, contextEntity);
        this.thenActions = thenActions;
        this.elseActions = elseActions;
        this.value = value;
        this.secondaryEntity = secondaryEntity;
    }

    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public ThenOrElse getThenActions() {
        return thenActions;
    }

    public ThenOrElse getElseActions() {
        return elseActions;
    }

    @Override
    public Object getValue() {
        return value.evaluate();
    }

    /**
     * Invokes all "thenActions" on the given entity instance.
     * If this is activated then this condition must be true.
     * @param entityInstance The given entity to invoke all "thenActions" upon.
     */
    protected void invokeThenActions(EntityInstance entityInstance, int lastChangTickCount){
        if(thenActions != null){
            thenActions.invoke(entityInstance, lastChangTickCount);
        }
        isTrue = true;
    }

    /**
     * Invokes all "elseActions" on the given entity instance.
     * If this is activated then this condition must be true.
     * @param entityInstance The given entity to invoke all "elseActions" upon.
     */
    protected void invokeElseActions(EntityInstance entityInstance, int lastChangTickCount){
        if(elseActions != null) {
            elseActions.invoke(entityInstance, lastChangTickCount);
        }
        isTrue = false;
    }

    public static class SecondaryEntity{
        private final String contextEntity;
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

}
