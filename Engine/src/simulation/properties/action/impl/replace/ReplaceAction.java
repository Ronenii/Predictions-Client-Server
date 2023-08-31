package simulation.properties.action.impl.replace;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;

public class ReplaceAction extends AbstractAction {
    private final String newEntityName;

    private final ReplaceActionType replaceType;

    public ReplaceAction(ActionType type, String property, String contextEntity, String newEntityName, ReplaceActionType replaceType) {
        super(type, property, contextEntity);
        this.newEntityName = newEntityName;
        this.replaceType = replaceType;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void Invoke(EntityInstance entityInstance, int lastChangeTickCount) {

    }
}
