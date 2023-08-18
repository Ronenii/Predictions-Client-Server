package simulation.properties.action.impl.condition;

import simulation.objects.entity.EntityInstance;
import simulation.properties.property.api.Property;

public class SingleCondition extends AbstractConditionAction{
    private final ConditionOperator operator;

    public SingleCondition(String property, String contextEntity, ThenOrElse thenActions, ThenOrElse elseActions, ConditionOperator operator, String contextValue) {
        super(property, contextEntity, thenActions, elseActions, contextValue);
        this.operator = operator;
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {
        Property toCompare = entityInstance.getPropertyByName(getContextProperty());

        if(toCompare == null){
            return;
        }

        switch (operator){
            case EQUALS:
                if(toCompare.getValue() == getValue())
                {
                    getThenActions().invoke(entityInstance);
                }
                else {
                    if(!getElseActions().getActionsToInvoke().isEmpty()){
                        getElseActions().invoke(entityInstance);
                    }
                }
                break;
            case NOT_EQUALS:
                if(toCompare.getValue() != getValue())
                {
                    getThenActions().invoke(entityInstance);
                }
                else {
                    if(!getElseActions().getActionsToInvoke().isEmpty()){
                        getElseActions().invoke(entityInstance);
                    }
                }
                break;
            case BIGGER_THAN:
                if((double)toCompare.getValue() > (double)getValue())
                {
                    getThenActions().invoke(entityInstance);
                }
                else {
                    if(!getElseActions().getActionsToInvoke().isEmpty()){
                        getElseActions().invoke(entityInstance);
                    }
                }
                break;
            case LESSER_THAN:
                if((double)toCompare.getValue() < (double)getValue())
                {
                    getThenActions().invoke(entityInstance);
                }
                else {
                    if(!getElseActions().getActionsToInvoke().isEmpty()){
                        getElseActions().invoke(entityInstance);
                    }
                }
                break;
        }
    }
}
