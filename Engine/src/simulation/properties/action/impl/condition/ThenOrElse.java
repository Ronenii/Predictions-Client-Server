package simulation.properties.action.impl.condition;

import simulation.properties.action.api.Action;

import java.util.Set;

public class ThenOrElse {

    // ask aviad if we need to invoke the actions in the order we got them, if he says yes, implement List instead of map.
    private Set<Action> actionsToInvoke;

    public ThenOrElse(Set<Action> actionsToInvoke) {
        this.actionsToInvoke = actionsToInvoke;
    }

    public void invoke(){
        // TODO: implement.
    }
}
