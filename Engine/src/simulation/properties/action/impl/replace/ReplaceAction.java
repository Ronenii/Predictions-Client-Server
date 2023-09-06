package simulation.properties.action.impl.replace;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.expression.api.Expression;

public class ReplaceAction extends AbstractAction {
    private final String newEntityName;

    private final ReplaceActionType replaceType;

    public ReplaceAction(Expression property, String contextEntity, String newEntityName, SecondaryEntity secondaryEntity, ReplaceActionType replaceType) {
        super(ActionType.REPLACE, property, contextEntity, secondaryEntity);
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


    public void invoke(EntityInstance firstEntityInstance, EntityInstance secondEntityInstance, int lastChangeTickCount) {
        if(replaceType == ReplaceActionType.DERIVED) {
            secondEntityInstance.updateDerivedEntityInstance(firstEntityInstance, lastChangeTickCount);
        }

        firstEntityInstance.kill();
    }
}
