package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.exception.CrashException;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.*;
import simulation.properties.action.expression.api.Expression;

import java.io.Serializable;


public abstract class AbstractConditionAction extends AbstractAction implements Serializable {
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
    protected void invokeThenActions(EntityInstance entityInstance, Grid grid, int lastChangTickCount) throws CrashException {
        if(thenActions != null){
            thenActions.invoke(entityInstance, grid, lastChangTickCount);
        }
        isTrue = true;
    }

    protected void invokeThenActionsWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        if(thenActions != null){
            thenActions.invokeWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
        }
        isTrue = true;
    }

    /**
     * Invokes all "elseActions" on the given entity instance.
     * If this is activated then this condition must be true.
     * @param entityInstance The given entity to invoke all "elseActions" upon.
     */
    protected void invokeElseActions(EntityInstance entityInstance, Grid grid, int lastChangTickCount) throws CrashException {
        if(elseActions != null) {
            elseActions.invoke(entityInstance, grid, lastChangTickCount);
        }
        isTrue = false;
    }

    protected void invokeElseActionsWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) throws CrashException {
        if(elseActions != null){
            elseActions.invokeWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
        }
        isTrue = false;
    }

    public Boolean getConditionResult(EntityInstance entityInstance) throws CrashException {
        this.invoke(entityInstance, null, 0);
        return isTrue;
    }

    abstract public void invoke(EntityInstance entityInstance, Grid grid, int lastChangeTickCount) throws CrashException;

    abstract public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) throws CrashException;
}
