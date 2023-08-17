package simulation.properties.action.impl.condition;


import java.util.List;

public class MultipleCondition extends AbstractConditionAction{
    private final String logical;

    private final List<AbstractConditionAction> subConditions;

    public MultipleCondition(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, String logical, List<AbstractConditionAction> conditions, String contextValue) {
        super(property, contextEntity, thenActions, elseActions, contextValue);
        this.logical = logical;
        this.subConditions = conditions;
    }

    public String getLogical() {
        return logical;
    }

    public List<AbstractConditionAction> getSubConditions() {
        return subConditions;
    }

    @Override
    public void Invoke() {

    }
}
