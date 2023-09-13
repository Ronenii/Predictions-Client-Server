package simulation.properties.action.impl.proximity;

import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.impl.calculation.CalculationAction;
import simulation.properties.action.impl.condition.AbstractConditionAction;
import simulation.properties.action.impl.replace.ReplaceAction;

import java.util.ArrayList;
import java.util.List;

public class ProximitySubActions {
    private final List<Action> actionsToInvoke;

    public ProximitySubActions(List<Action> actionsToInvoke) {
        this.actionsToInvoke = actionsToInvoke;
    }

    public List<Action> getActionsToInvoke() {
        return actionsToInvoke;
    }

    public int getSubActionsCount(){
        return actionsToInvoke.size();
    }

    public void invoke(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance, Grid grid, int lastChangTickCount){
        for (Action action: actionsToInvoke) {
            if(action instanceof OneEntAction){
                OneEntAction oneEntAction = (OneEntAction)action;
                oneEntAction.invokeWithSecondary(sourceEntityInstance, sourceEntityInstance, lastChangTickCount);
            } else if (action instanceof CalculationAction) {
                CalculationAction calculationAction = (CalculationAction)action;
                calculationAction.invokeWithSecondary(sourceEntityInstance,targetEntityInstance, lastChangTickCount);
            } else if (action instanceof AbstractConditionAction) {
                AbstractConditionAction abstractConditionAction = (AbstractConditionAction) action;
                abstractConditionAction.invokeWithSecondary(sourceEntityInstance, targetEntityInstance, grid, lastChangTickCount);
            } else if(action instanceof ProximityAction) {
                ProximityAction proximityAction = (ProximityAction)action;
                proximityAction.invokeWithSecondary(sourceEntityInstance, targetEntityInstance, grid, lastChangTickCount);
            } else if(action instanceof ReplaceAction) {
                ReplaceAction replaceAction = (ReplaceAction)action;
                replaceAction.invokeWithSecondary(sourceEntityInstance, targetEntityInstance, grid, lastChangTickCount);
            }
        }
    }

    public ProximitySubActions dupProximitySubActions() {
        List<Action> actionsToInvokeDup = new ArrayList<>();

        for (Action action : actionsToInvoke) {
            actionsToInvokeDup.add(action.dupAction());
        }

        return new ProximitySubActions(actionsToInvokeDup);
    }
}
