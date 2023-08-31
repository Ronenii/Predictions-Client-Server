package simulation.properties.action.impl.proximity;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.Action;

import java.util.List;

public class ProximityActions {
    private List<Action> actionsToInvoke;

    public ProximityActions(List<Action> actionsToInvoke) {
        this.actionsToInvoke = actionsToInvoke;
    }

    public void invoke(EntityInstance sourceEntityInstance, EntityInstance targetEntityInstance, int lastChangTickCount){
        for (Action a: actionsToInvoke) {

        }
    }
}
