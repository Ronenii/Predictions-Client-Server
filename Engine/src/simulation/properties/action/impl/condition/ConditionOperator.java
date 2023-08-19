package simulation.properties.action.impl.condition;

import java.util.Objects;

public enum ConditionOperator {
    EQUALS, NOT_EQUALS, BIGGER_THAN, LESSER_THAN;

    public static ConditionOperator tryParse(String operatorString){
        if(Objects.equals(operatorString, "bt")){return BIGGER_THAN;}
        if(Objects.equals(operatorString, "lt")) {return  LESSER_THAN;}
        if(Objects.equals(operatorString, "=")) { return  EQUALS;}
        if(Objects.equals(operatorString, "!=")) { return NOT_EQUALS;}

        throw new IllegalArgumentException("Invalid operator string");
    }
}
