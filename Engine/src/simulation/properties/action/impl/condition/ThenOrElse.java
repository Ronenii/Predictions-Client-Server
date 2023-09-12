package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.impl.proximity.ProximityAction;
import simulation.properties.action.impl.replace.ReplaceAction;

import java.io.Serializable;
import java.util.List;

public class ThenOrElse implements Serializable {
    private final List<Action> actionsToInvoke;

    public ThenOrElse(List<Action> actionsToInvoke) {
        this.actionsToInvoke = actionsToInvoke;
    }

    public int getActionsCount(){
        return actionsToInvoke.size();
    }

    public void invoke(EntityInstance entityInstance, Grid grid, int lastChangTickCount){
        for (Action a: actionsToInvoke) {
            invokeAnAction(a, entityInstance, grid, lastChangTickCount);
        }
    }

    private void invokeAnAction(Action action, EntityInstance entityInstance, Grid grid, int lastChangTickCount) {
        if(action instanceof OneEntAction){
            OneEntAction oneEntAction = (OneEntAction)action;
            oneEntAction.invoke(entityInstance, lastChangTickCount);
        } else if (action instanceof AbstractConditionAction) {
            AbstractConditionAction abstractConditionAction = (AbstractConditionAction) action;
            abstractConditionAction.invoke(entityInstance, grid, lastChangTickCount);
        } else if(action instanceof ProximityAction) {
            ProximityAction proximityAction = (ProximityAction)action;
            proximityAction.invoke(entityInstance, grid, lastChangTickCount);
        } else if(action instanceof ReplaceAction) {
            ReplaceAction replaceAction = (ReplaceAction)action;
            replaceAction.invoke(entityInstance, grid, lastChangTickCount);
        }
    }

    public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) {
        for (Action a: actionsToInvoke) {
            if(a.getContextEntity().equals(primaryInstance.getInstanceEntityName())) {
                invokeAnAction(a, primaryInstance, grid, lastChangeTickCount);
            }
            else {
                invokeAnAction(a, secondaryInstance, grid, lastChangeTickCount);
            }
        }
    }
}
