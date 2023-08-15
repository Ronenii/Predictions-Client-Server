package jaxb.unmarshal.converter.expression.converter;

import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDCondition;
import jaxb.unmarshal.converter.api.Validator;
import jaxb.unmarshal.converter.functions.HelperFunctionsType;
import jaxb.unmarshal.converter.functions.StaticHelperFunctions;
import simulation.objects.entity.Entity;
import simulation.properties.action.api.ActionType;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

import java.util.Map;

public class ExpressionConverterAndValidator extends Validator {

    private final Map<String, Property> environmentProperties;
    private final Map<String, Entity> entities;

    public ExpressionConverterAndValidator(Map<String, Property> environmentProperties, Map<String, Entity> entities) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
    }

    /**
     * Analyze the value string from the PRDAction, in case the given string represent a function, a property or just a regular value.
     * The method also checks whether the object matches the property's value type and the action's type.
     * if yes, the method return the requested object
     * Otherwise, an exception thrown in order to stop this action object creation.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file
     * @param prdValueStr the given value name from the given PRDTAction generated from reading the XML file.
     * The name sent separately in order to analyze the two arguments of 'Calculation' action too.
     * @return the value requested object.
     */
    public Object analyzeAndGetValue(PRDAction prdAction, String prdValueStr){
        Object value;
        value = getObjectIfFunction(prdValueStr);
        if(value == null){
            value = getIfProperty(prdAction, prdValueStr);
        }
        if(value == null){
            value = parseValue(prdValueStr);
        }
        if(!compareActionValueToGivenPropertyValue(prdAction, value)){
            // validation error occurred.
            throw new RuntimeException();
        }

        return value;
    }

    /**
     * Check if the PRDAction value is a function name and return the value from the given function.
     * If the value is not a function name, the return value will be null.
     *
     * @param prdValueStr the PRDAction value string.
     * @return the return value from the function if exists.
     */
    private Object getObjectIfFunction(String prdValueStr){
        String functionName = getFucntionName(prdValueStr);
        Object ret = null;
        try{
            switch (HelperFunctionsType.valueOf(functionName)){
                case ENVIRONMENT:
                    ret = StaticHelperFunctions.environment(getFunctionParam(prdValueStr), environmentProperties);
                case RANDOM:
                    ret = StaticHelperFunctions.random(Integer.parseInt(getFunctionParam(prdValueStr)));
                case EVALUATE:
                    ret = null;
                case PERCENT:
                    ret = null;
                case TICKS:
                    ret = null;
            }
        }
        catch (Exception e) {
            // Value is not a function
            ret = null;
        }

        return ret;
    }

    /**
     * Check if the PRDAction value is a property name and return the property object.
     * If the value is not a property name, the return value will be null.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file
     * @param prdValueStr the PRDAction value string.
     * @return the requested property if exists.
     */
    private Property getIfProperty(PRDAction prdAction, String prdValueStr) {
        String entityName = prdAction.getEntity();
        Entity entity = entities.get(entityName);
        return entity.getProperties().get(prdValueStr);
    }

    /**
     * If 'analyzeAndGetValue' enter this method, the given value from the XML is not a function or a property.
     * This method parse the string to one of the following types: Integer, Double, Boolean or String.
     *
     * @param prdValueStr the PRDAction value string.
     * @return the given value string parse into one of the four types.
     */
    private Object parseValue(String prdValueStr){
        Boolean flag = false;
        Object ret = null;

        ret = getIntOrDouble(prdValueStr, flag);
        if(flag){
            ret = getBooleanOrStr(prdValueStr);
        }

        return ret;
    }

    /**
     * 'parseValue' helper, the boolean flag is an input member in order to change his value for the next checks outside this method.
     */
    private Object getIntOrDouble(String prdValueStr, Boolean flag){
        Object ret = null;

        try{
            ret = Integer.parseInt(prdValueStr);
        }
        catch (NumberFormatException e){
            flag = true;
        }
        if(flag){
            try{
                ret = Double.parseDouble(prdValueStr);
            }
            catch (NumberFormatException e) {
                flag = true;
            }
        }

        return ret;
    }

    /**
     * 'parseValue' helper.
     */
    private Object getBooleanOrStr(String prdValueStr){
        Object ret;
        String prdValueToLower = prdValueStr.toLowerCase();
        if(prdValueToLower.equals("true")){
            ret = true;
        } else if (prdValueToLower.equals("false")) {
            ret = false;
        }
        else {
            ret = prdValueStr;
        }

        return ret;
    }

    /**
     * Extract the function name from the given value string if a function name exists in the string.
     * Otherwise, return null.
     *
     * @param prdValueStr the given value from the given PRDTAction generated from reading the XML file
     * @return the function name in the given string.
     */
    private String getFucntionName(String prdValueStr){
        String ret = null;
        int openParenIndex = prdValueStr.indexOf("(");

        if (openParenIndex != -1) {
            ret = prdValueStr.substring(0, openParenIndex);
        }

        return ret;
    }

    /**
     * Extract the function's params from the given value string if the params exist in the string.
     * Otherwise, return null.
     *
     * @param prdValueStr the given value from the given PRDTAction generated from reading the XML file
     * @return the functions params in the given string.
     */
    private String getFunctionParam(String prdValueStr){
        String ret = null;
        int openParenIndex = prdValueStr.indexOf("(");
        int closeParenIndex = prdValueStr.indexOf(")");

        if (openParenIndex != -1 && closeParenIndex != -1 && closeParenIndex > openParenIndex) {
            ret = prdValueStr.substring(openParenIndex + 1, closeParenIndex);
        }

        return ret;
    }

    /**
     * After the object has been decodes, this method checks whether the object matches the property's value type and the action's type.
     * If not, return false.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file
     * @param value the action value object.
     * @return true if the object complete the checks successfully, otherwise return false.
     */
    private boolean compareActionValueToGivenPropertyValue(PRDAction prdAction, Object value){
        boolean ret = true;

        if (value instanceof Integer) {
            ret = compareIntegerOrDoubleCase(prdAction);
        } else if (value instanceof Double) {
            ret = compareIntegerOrDoubleCase(prdAction);
        } else if (value instanceof Boolean) {
            ret = compareBooleanCase(prdAction);
        } else if (value instanceof String) {
            ret = compareStringCase(prdAction);
        }

        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for integer or double actions/properties.
     */
    private boolean compareIntegerOrDoubleCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type != ActionType.INCREASE && type != ActionType.DECREASE && type != ActionType.CALCULATION && type != ActionType.CONDITION)
        {
            addErrorToList(prdAction, prdAction.getValue(), "Action type not allowed");
            ret = false;
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("INT")) && (!propertyType.name().equals("DOUBLE"))){
            addErrorToList(entity.getProperties().get(propertyName), propertyType.name(), "The property value type doesn't match the action value type");
            ret= false;
        }

        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for boolean actions/properties.
     */
    private boolean compareBooleanCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        PRDCondition prdCondition;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            addErrorToList(prdAction, prdAction.getValue(), "Action type not allowed");
            ret = false;
        }

        if(type == ActionType.CONDITION){
            prdCondition = prdAction.getPRDCondition();
            if(prdCondition.getSingularity().equals("single") && (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))){
                addErrorToList(prdAction, prdAction.getValue(), "Condition operator type not allowed");
                ret = false;
            }
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("BOOLEAN"))){
            addErrorToList(prdAction, prdAction.getValue(), "The property value type doesn't match the action value type");
            ret = false;
        }

        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for String actions/properties.
     */
    private boolean compareStringCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            addErrorToList(prdAction, prdAction.getValue(), "Action type not allowed");
            ret = false;
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("STRING"))){
            addErrorToList(prdAction, prdAction.getValue(), "The property value type doesn't match the action value type");
            ret = false;
        }

        return ret;
    }
}
