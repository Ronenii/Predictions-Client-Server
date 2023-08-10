package properties.action.impl.condition;

import properties.action.api.AbstractAction;
import properties.action.api.ActionType;
import properties.property.api.Property;


public abstract class AbstractConditionAction extends AbstractAction {
    private final Object value;
    private final ThenOrElse thenActions;
    private final ThenOrElse elseActions;

    public AbstractConditionAction(String property, String contextEntity, Object value, ThenOrElse thenActions, ThenOrElse elseActions) {
        super(ActionType.CONDITION, property, contextEntity);
        this.value = value;
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }
}
