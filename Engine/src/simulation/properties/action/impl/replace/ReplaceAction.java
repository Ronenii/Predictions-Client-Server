package simulation.properties.action.impl.replace;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.api.TwoEntAction;

public class ReplaceAction extends TwoEntAction {
    private final String newEntityName;

    private final ReplaceActionType replaceType;

    public ReplaceAction(String property, String contextEntity, String newEntityName, ReplaceActionType replaceType) {
        super(ActionType.REPLACE, property, contextEntity);
        this.newEntityName = newEntityName;
        this.replaceType = replaceType;
    }

    public String getNewEntityName() {
        return newEntityName;
    }

    @Override
    public Object getValue() {
        return null;
    }


    public void Invoke(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, int lastChangeTickCount) {
        if(replaceType == ReplaceActionType.DERIVED) {
            secondEntityInstance.updateDerivedEntityInstance(firstEntityInstance, lastChangeTickCount);
        }

        firstEntityInstance.kill();
    }
}
