package properties.action.impl.condition;


import properties.property.api.Property;

public class MultipleCondition extends AbstractConditionAction{
    private final String logical;

    public MultipleCondition(String property, String contextEntity, Object value, ThenOrElse thenActions, ThenOrElse elseActions, String logical) {
        super(property, contextEntity, value, thenActions, elseActions);
        this.logical = logical;
    }

    @Override
    public void Invoke() {

    }
}
