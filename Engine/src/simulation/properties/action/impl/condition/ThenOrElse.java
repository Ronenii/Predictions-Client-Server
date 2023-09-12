package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.impl.calculation.CalculationAction;
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
        for (Action action: actionsToInvoke) {
            if(action instanceof OneEntAction){
                OneEntAction oneEntAction = (OneEntAction)action;
                oneEntAction.invoke(entityInstance, false, lastChangTickCount);
            } else if(action instanceof CalculationAction){
                CalculationAction calculationAction = (CalculationAction)action;
                calculationAction.invoke(entityInstance,false,false,lastChangTickCount);
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
    }

    public void invokeWithSecondary(EntityInstance primaryInstance, EntityInstance secondaryInstance, Grid grid, int lastChangeTickCount) {
        for (Action action: actionsToInvoke) {
            if(action instanceof OneEntAction){
                OneEntAction oneEntAction = (OneEntAction)action;
                oneEntAction.invokeWithSecondary(primaryInstance, secondaryInstance, lastChangeTickCount);
            } else if (action instanceof CalculationAction) {
                CalculationAction calculationAction = (CalculationAction)action;
                calculationAction.invokeWithSecondary(primaryInstance,secondaryInstance, lastChangeTickCount);
            } else if (action instanceof AbstractConditionAction) {
                AbstractConditionAction abstractConditionAction = (AbstractConditionAction) action;
                abstractConditionAction.invokeWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
            } else if(action instanceof ProximityAction) {
                ProximityAction proximityAction = (ProximityAction)action;
                proximityAction.invokeWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
            } else if(action instanceof ReplaceAction) {
                ReplaceAction replaceAction = (ReplaceAction)action;
                replaceAction.invokeWithSecondary(primaryInstance, secondaryInstance, grid, lastChangeTickCount);
            }
        }
    }
}
