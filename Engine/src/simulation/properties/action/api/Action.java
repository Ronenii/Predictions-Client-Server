package simulation.properties.action.api;

import simulation.objects.entity.EntityInstance;
import simulation.properties.action.expression.api.Expression;


public interface Action {
    ActionType getType();

    String getContextEntity();

    Expression getContextProperty();

    Object getValue();

    Expression getValueExpression();

    AbstractAction.SecondaryEntity getSecondaryEntity();

}
