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

    public AbstractConditionAction(ActionType type, Expression contextProperty, String contextEntity, SecondaryEntity secondaryEntity, Expression value, ThenOrElse thenActions, ThenOrElse elseActions) {
        super(type, contextProperty, contextEntity, secondaryEntity);
        this.value = value;
        this.thenActions = thenActions;
        this.elseActions = elseActions;
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

    public int getThenActionsCount(){
        int ret = 0;

        if(thenActions != null){
            ret = thenActions.getActionsCount();
        }
        return ret;
    }

    public int getElseActionsCount(){
        int ret = 0;

        if(elseActions != null){
            ret = elseActions.getActionsCount();
        }
        return ret;
    }

    @Override
    public Object getValue() {
        return value.evaluate();
    }

    @Override
    public Expression getValueExpression() {
        return value;
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

    public Boolean getConditionResult(EntityInstance entityInstance) {
        this.invoke(entityInstance, 0);
        return isTrue;
    }
}
