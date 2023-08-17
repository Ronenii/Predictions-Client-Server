package simulation.properties.action.impl.condition;

import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;


public abstract class AbstractConditionAction extends AbstractAction {
    private Object value;
    private final ThenOrElse thenActions;
    private final ThenOrElse elseActions;

    public AbstractConditionAction(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, String contextValue) {
        super(ActionType.CONDITION, property, contextEntity, contextValue);
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }

    public void updateValue(Object value){
        this.value = value;
    }
}
