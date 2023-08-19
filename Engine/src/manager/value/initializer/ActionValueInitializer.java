package manager.value.initializer;

import manager.value.functions.HelperFunctionsType;
import manager.value.functions.StaticHelperFunctions;
import manager.value.update.object.api.UpdateObject;
import manager.value.update.object.impl.OneObjectUpdate;
import manager.value.update.object.impl.TwoObjectUpdate;
import simulation.objects.entity.Entity;
import simulation.properties.action.api.Action;
import simulation.properties.action.impl.KillAction;
import simulation.properties.action.impl.calculation.CalculationAction;
import simulation.properties.action.impl.condition.AbstractConditionAction;
import simulation.properties.action.impl.condition.MultipleCondition;
import simulation.properties.action.impl.condition.SingleCondition;
import simulation.properties.action.impl.condition.ThenOrElse;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

import java.util.List;
import java.util.Map;


public class ActionValueInitializer {
    private final Map<String,Property> environmentVars;
    private final Map<String, Entity> entities;

    public ActionValueInitializer(Map<String, Property> environmentVars, Map<String, Entity> entities) {
        this.environmentVars = environmentVars;
        this.entities = entities;
    }

    /**
     * Receive a list of rule's actions and initial each action's value by using the context value from the XML file.
     *
     * @param actions a list of specific rule's actions
     */
    public void initializeValues(List<Action> actions){
        UpdateObject updateObject;
        Property property;

        for (Action action : actions){
            if(action.getClass() != KillAction.class){
                if(action.getClass() == MultipleCondition.class){
                    setMultipleCase((MultipleCondition)action);
                } else if (action.getClass() == CalculationAction.class) {
                    setCalculationCase((CalculationAction)action);
                } else if (action.getClass() == SingleCondition.class) {
                    setSingleCase((SingleCondition)action);
                } else {
                    property = entities.get(action.getContextEntity()).getProperties().get(action.getContextProperty());
                    updateObject = new OneObjectUpdate(convertContextValue(action.getContextValue(),property.getType(),action.getContextEntity()));
                    action.updateValue(updateObject);
                }
            }
        }
    }

    /**
     * Handling the value initialize of a single condition object, including the then/else actions.
     */
    private void setSingleCase(SingleCondition condition){
        UpdateObject updateObject;
        Property property;
        ThenOrElse thenActions = condition.getThenActions(), elseActions = condition.getElseActions();

        if (thenActions != null){
            initializeValues(thenActions.getActionsToInvoke());
        }
        if (elseActions != null) {
            initializeValues(elseActions.getActionsToInvoke());
        }

        property = entities.get(condition.getContextEntity()).getProperties().get(condition.getContextProperty());
        updateObject = new OneObjectUpdate(convertContextValue(condition.getContextValue(),property.getType(),condition.getContextEntity()));
        condition.updateValue(updateObject);
    }

    /**
     * Handling the value initialize of a multiple condition object, including the then/else actions.
     */
    private void setMultipleCase(MultipleCondition condition) {
        List<AbstractConditionAction> subConditions = condition.getSubConditions();
        ThenOrElse thenActions = condition.getThenActions(), elseActions = condition.getElseActions();

        if (thenActions != null){
            initializeValues(thenActions.getActionsToInvoke());
        }
        if (elseActions != null) {
            initializeValues(elseActions.getActionsToInvoke());
        }

        for (AbstractConditionAction subCondition : subConditions){
            if(subCondition.getClass() == SingleCondition.class) {
                setSingleCase((SingleCondition)subCondition);
            }
            else {
                setMultipleCase((MultipleCondition)subCondition);
            }
        }
    }

    /**
     * Handling the value initialize of a calculation condition object.
     */
    private void setCalculationCase(CalculationAction action) {
        String obj1 = action.getArg1().toString(), obj2 = action.getArg2().toString();
        Property property = entities.get(action.getContextEntity()).getProperties().get(action.getContextProperty());
        UpdateObject updateObject;
        Object arg1, arg2;

        arg1 = convertContextValue(obj1, property.getType(), action.getContextEntity());
        arg2 = convertContextValue(obj2, property.getType(), action.getContextEntity());
        updateObject = new TwoObjectUpdate(arg1, arg2);
        action.updateValue(updateObject);
    }

    private Object convertContextValue(String contextValue, PropertyType type, String entityName) {
        Object ret;

        ret = getObjectIfFunction(contextValue);
        if(ret == null) {
            ret = getTypeIfProperty(contextValue, entityName);
        }
        if(ret == null){
            ret = parseValue(contextValue,type);
        }

        return ret;
    }

    /**
     * Check if the valueStr value is a function name and return the value type of the given function.
     * If the value is not a function name, the return value will be null.
     *
     * @param valueStr the PRDAction value string.
     * @return the return value from the function if exists.
     */
    private Object getObjectIfFunction(String valueStr){
        String functionName = getFucntionName(valueStr);
        Object ret = null;
        if(functionName != null) {
            String param = getFunctionParam(valueStr);
            switch (HelperFunctionsType.valueOf(functionName.toUpperCase())) {
                case ENVIRONMENT:
                    ret = StaticHelperFunctions.environment(param, environmentVars);
                    break;
                case RANDOM:
                    ret = StaticHelperFunctions.random(Integer.parseInt(param));
                    break;
                case EVALUATE:
                    ret = null;
                    break;
                case PERCENT:
                    ret = null;
                    break;
                case TICKS:
                    ret = null;
                    break;
            }
        }

        return ret;
    }

    /**
     * Extract the function name from the given value string if a function name exists in the string.
     * Otherwise, return null.
     *
     * @param valueStr the given value from the given PRDTAction generated from reading the XML file
     * @return the function name in the given string.
     */
    private String getFucntionName(String valueStr){
        String ret = null;
        int openParenIndex = valueStr.indexOf("(");

        if (openParenIndex != -1) {
            ret = valueStr.substring(0, openParenIndex);
        }

        return ret;
    }

    /**
     * Extract the function's params from the given value string if the params exist in the string.
     * Otherwise, return null.
     *
     * @param valueStr the given value from the given PRDTAction generated from reading the XML file
     * @return the functions params in the given string.
     */
    private String getFunctionParam(String valueStr){
        String ret = null;
        int openParenIndex = valueStr.indexOf("(");
        int closeParenIndex = valueStr.indexOf(")");

        if (openParenIndex != -1 && closeParenIndex != -1 && closeParenIndex > openParenIndex) {
            ret = valueStr.substring(openParenIndex + 1, closeParenIndex);
        }

        return ret;
    }

    /**
     * Check if the valueStr value is a property name and return the property value object.
     * If the value is not a property name, the return value will be null.
     *
     * @param valueStr the PRDAction value string.
     * @return the requested property if exists.
     */
    private Object getTypeIfProperty(String valueStr, String entityName) {
        Entity entity = entities.get(entityName);
        Property property = entity.getProperties().get(valueStr);
        Object ret = null;

        if(property != null){
            ret = property.getValue();
        }

        return ret;
    }

    /**
     * Parse 'valueStr' according to the property type.
     */
    private Object parseValue(String valueStr, PropertyType type){
        Object ret = null;

        switch (type) {
            case DECIMAL:
                ret = Integer.parseInt(valueStr);
                break;
            case FLOAT:
                ret = Double.parseDouble(valueStr);
                break;
            case BOOLEAN:
                if(valueStr.equals("true")){
                    ret = true;
                }
                else {
                    ret = false;
                }
                break;
            case STRING:
                ret = valueStr;
                break;
        }

        return ret;
    }

}
