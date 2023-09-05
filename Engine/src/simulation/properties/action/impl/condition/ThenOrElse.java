package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
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

    public void invoke(EntityInstance entityInstance, int lastChangTickCount){
        for (Action a: actionsToInvoke
             ) {
            // TODO: Find a way to implement this.
            if(a instanceof OneEntAction){
                OneEntAction action = (OneEntAction)a;
                //action.invoke();
            }
            if(a instanceof ProximityAction){
                ProximityAction action = (ProximityAction)a;
                //action.invoke();
            }
            if(a instanceof ReplaceAction){
                ReplaceAction action = (ReplaceAction)a;
                //action.invoke();
            }
        }
    }
}
