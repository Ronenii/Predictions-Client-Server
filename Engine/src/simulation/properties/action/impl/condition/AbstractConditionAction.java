package simulation.properties.action.impl.condition;

import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;


public abstract class AbstractConditionAction extends AbstractAction {
    protected Object value;

    protected boolean isTrue;
    protected final ThenOrElse thenActions;
    protected final ThenOrElse elseActions;

    public AbstractConditionAction(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, String contextValue) {
        super(ActionType.CONDITION, property, contextEntity, contextValue);
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }

    @Override
    public void updateValue(UpdateObject updateObject) {
        OneObjectUpdate oneObjectUpdate = (OneObjectUpdate)updateObject;
        value = oneObjectUpdate.getObjectForUpdate();
    }

    public ThenOrElse getThenActions() {
        return thenActions;
    }

    public ThenOrElse getElseActions() {
        return elseActions;
    }

    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Invokes all "thenActions" on the given entity instance.
     * If this is activated then this condition must be true.
     * @param entityInstance The given entity to invoke all "thenActions" upon.
     */
    protected void invokeThenActions(EntityInstance entityInstance){
        if(thenActions != null){
            thenActions.invoke(entityInstance);
        }
        isTrue = true;
    }

    /**
     * Invokes all "elseActions" on the given entity instance.
     * If this is activated then this condition must be true.
     * @param entityInstance The given entity to invoke all "elseActions" upon.
     */
    protected void invokeElseActions(EntityInstance entityInstance){
        if(elseActions != null) {
            elseActions.invoke(entityInstance);
        }
        isTrue = false;
    }

    public void updateValue(Object value){
        this.value = value;
    }
}
