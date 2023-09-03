package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.impl.proximity.ProximityAction;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class ThenOrElse implements Serializable {


    private List<Action> actionsToInvoke;

    public ThenOrElse(List<Action> actionsToInvoke) {
        this.actionsToInvoke = actionsToInvoke;
    }

    public void invoke(EntityInstance entityInstance, int lastChangTickCount){
        for (Action a: actionsToInvoke
             ) {
        }
    }
}
