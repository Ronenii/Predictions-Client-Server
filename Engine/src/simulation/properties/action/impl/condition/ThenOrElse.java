package simulation.properties.action.impl.condition;

import simulation.properties.action.api.Action;

import java.util.List;
import java.util.Set;

public class ThenOrElse {


    private List<Action> actionsToInvoke;

    public ThenOrElse(List<Action> actionsToInvoke) {
        this.actionsToInvoke = actionsToInvoke;
    }

    public List<Action> getActionsToInvoke() {
        return actionsToInvoke;
    }

    public void invoke(){
        // TODO: implement.
    }
}
