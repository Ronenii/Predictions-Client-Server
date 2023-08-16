package simulation.properties.action.impl.condition;


import java.util.List;

public class MultipleCondition extends AbstractConditionAction{
    private final String logical;

    private final List<AbstractConditionAction> subConditions;

    public MultipleCondition(String property, String contextEntity, Object value, ThenOrElse thenActions, ThenOrElse elseActions, String logical, List<AbstractConditionAction> conditions) {
        super(property, contextEntity, value, thenActions, elseActions);
        this.logical = logical;
        this.subConditions = conditions;
    }

    @Override
    public void Invoke() {

    }
}
