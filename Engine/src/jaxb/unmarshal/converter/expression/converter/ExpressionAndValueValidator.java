package jaxb.unmarshal.converter.expression.converter;

import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDCondition;
import jaxb.schema.generated.PRDProperty;
import jaxb.unmarshal.converter.api.Validator;
import jaxb.unmarshal.converter.expression.converter.exception.ExpressionConversionException;
import manager.value.functions.HelperFunctionsType;
import simulation.objects.entity.Entity;
import simulation.properties.action.api.ActionType;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

import java.util.Map;

public class ExpressionAndValueValidator extends Validator {

    private final Map<String, Property> environmentProperties;
    private final Map<String, Entity> entities;

    public ExpressionAndValueValidator(Map<String, Property> environmentProperties, Map<String, Entity> entities) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
    }

    public boolean isPRDPropertyValueMatchItsType(PRDProperty prdProperty){
        String prdPropertyValue = prdProperty.getPRDValue().getInit(), prdPropertyType = prdProperty.getType().toUpperCase(), givenValueType;
        boolean ret = true;

        if(!prdProperty.getPRDValue().isRandomInitialize()){
            givenValueType = parseValueType(prdPropertyValue);
            if(!givenValueType.equals(prdPropertyType)) {
                ret = false;
            }
        }

        return ret;
    }

    /**
     * Analyze the value string from the PRDAction or PRDCondition in case the given string represent a function, a property or just a regular value.
     * The method also checks whether the object matches the property's value type and the action's type.
     * if yes, the method return the requested object
     * Otherwise, an exception thrown in order to stop this action object creation.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file, if the purpose is to create the value from the prdCondition, this param will set to null
     * @param prdValueStr the given value name from the given PRDTAction generated from reading the XML file.
     * @param prdCondition the given PRDCondition generated from reading the XML file, if the purpose is to create the value from the prdAction, this param will set to null
     * The name sent separately in order to analyze the two arguments of 'Calculation' action too.
     * @return the value requested object.
     */
    public void isPRDActionValueMatchItsPropertyType(PRDAction prdAction, PRDCondition prdCondition, String prdValueStr) throws ExpressionConversionException {
        String valueType;
        valueType = getObjectTypeIfFunction(prdValueStr, prdAction, prdCondition);
        if(valueType == null){
            valueType = getTypeIfProperty(prdAction, prdCondition, prdValueStr);
        }
        if(valueType == null){
            valueType = parseValueType(prdValueStr);
        }
        if(valueType == null || !compareActionValueToGivenPropertyValue(prdAction, prdCondition, valueType)){
            // validation error occurred.
            throw new ExpressionConversionException();
        }
    }

    /**
     * Check if the PRDAction value is a function name and return the value type of the given function.
     * If the value is not a function name, the return value will be null.
     *
     * @param prdValueStr the PRDAction value string.
     * @return the return value from the function if exists.
     */
    private String getObjectTypeIfFunction(String prdValueStr, PRDAction prdAction, PRDCondition prdCondition){
        String functionName = getFucntionName(prdValueStr);
        String ret = null;
        if(functionName != null){
            try{
                String param = getFunctionParam(prdValueStr);
                switch (HelperFunctionsType.valueOf(functionName.toUpperCase())){
                    case ENVIRONMENT:
                        Property checkStr = environmentProperties.get(param);
                        if(checkStr != null){
                            ret = checkStr.getType().toString();
                        }
                        else {
                            throw new Exception();
                        }
                        break;
                    case RANDOM:
                        int checkInt = Integer.parseInt(param);
                        ret = "DECIMAL";
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
            catch (Exception e) {
                // Value is not a function
                if(prdAction != null){
                    addErrorToList(prdAction, prdAction.getValue(), "The value function's param doesn't match to the function.");
                }
                else {
                    addErrorToList(prdCondition, prdCondition.getValue(), "The value function's param doesn't match to the function.");
                }
                ret = null;
            }
        }

        return ret;
    }

    /**
     * Check if the PRDAction value is a property name and return the property value type.
     * If the value is not a property name, the return value will be null.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file
     * @param prdValueStr the PRDAction value string.
     * @return the requested property if exists.
     */
    private String getTypeIfProperty(PRDAction prdAction, PRDCondition prdCondition, String prdValueStr) {
        String entityName;

        if(prdCondition == null){
            entityName = prdAction.getEntity();
        }
        else {
            entityName = prdCondition.getEntity();
        }

        Entity entity = entities.get(entityName);
        Property property = entity.getProperties().get(prdValueStr);
        String ret = null;

        if(property != null){
            ret = property.getType().toString();
        }

        return ret;
    }

    /**
     * If 'analyzeAndGetValue' enter this method, the given value from the XML is not a function or a property.
     * This method parse the string to one of the following types: Integer, Double, Boolean or String.
     *
     * @param prdValueStr the PRDAction value string.
     * @return the given value string parse into one of the four types.
     */
    private String parseValueType(String prdValueStr){
        String ret;

        ret = getIntOrDoubleType(prdValueStr);
        if(ret == null){
            ret = getBooleanOrStrType(prdValueStr);
        }

        return ret;
    }

    /**
     * 'parseValue' helper.
     */
    private String getIntOrDoubleType(String prdValueStr){
        Object test = null;
        String ret;
        boolean flag = false;

        try{
            test = Integer.parseInt(prdValueStr);
            ret = "DECIMAL";
        }
        catch (NumberFormatException e){
            flag = true;
            ret = null;
        }
        if(flag){
            try{
                test = Double.parseDouble(prdValueStr);
                ret = "FLOAT";
            }
            catch (NumberFormatException e) {
                ret = null;
            }
        }

        return ret;
    }

    /**
     * 'parseValue' helper.
     */
    private String getBooleanOrStrType(String prdValueStr){
        String ret;

        String prdValueToLower = prdValueStr.toLowerCase();
        if(prdValueToLower.equals("true") || prdValueToLower.equals("false")) {
            ret = "BOOLEAN";
        }else {
            ret = "STRING";
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
     * @param valueType the action value object.
     * @return true if the object complete the checks successfully, otherwise return false.
     */
    private boolean compareActionValueToGivenPropertyValue(PRDAction prdAction, PRDCondition prdCondition, String valueType){
        boolean ret = true;
        PropertyType type = PropertyType.valueOf(valueType);

        if(type == PropertyType.DECIMAL || type == PropertyType.FLOAT){
            if(prdAction != null){
                ret = compareIntegerOrDoubleCaseForAction(prdAction);
            }
            else {
                compareIntegerOrDoubleCaseForCondition(prdCondition);
            }
        }
        else {
            switch (PropertyType.valueOf(valueType)){
                case BOOLEAN:
                    if(prdAction != null){
                        ret = compareBooleanCaseForAction(prdAction);
                    }
                    else {
                        ret = compareBooleanCaseForCondition(prdCondition);
                    }

                    break;
                case STRING:
                    if (prdAction != null){
                        ret = compareStringCaseForAction(prdAction);
                    }
                    else {
                        ret = compareStringCaseForCondition(prdCondition);
                    }
                    break;
            }
        }
        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for integer or double actions/properties.
     */
    private boolean compareIntegerOrDoubleCaseForAction(PRDAction prdAction){
        String actionType, entityName, propertyName;

        actionType = prdAction.getType().toUpperCase();
        entityName = prdAction.getEntity();
        propertyName = prdAction.getProperty();

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
        if ((!propertyType.name().equals("DECIMAL")) && (!propertyType.name().equals("FLOAT"))){
            addErrorToList(entity.getProperties().get(propertyName), propertyType.name(), "The property value type doesn't match the action value type");
            ret= false;
        }

        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for integer or double actions/properties.
     */
    private boolean compareIntegerOrDoubleCaseForCondition(PRDCondition prdCondition){
        String actionType, entityName, propertyName;

        actionType = ActionType.CONDITION.toString();
        entityName = prdCondition.getEntity();
        propertyName = prdCondition.getProperty();

        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type != ActionType.INCREASE && type != ActionType.DECREASE && type != ActionType.CALCULATION && type != ActionType.CONDITION)
        {
            addErrorToList(prdCondition, prdCondition.getValue(), "Action type not allowed");
            ret = false;
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("DECIMAL")) && (!propertyType.name().equals("FLOAT"))){
            addErrorToList(entity.getProperties().get(propertyName), propertyType.name(), "The property value type doesn't match the action value type");
            ret= false;
        }

        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for boolean actions/properties.
     */
    private boolean compareBooleanCaseForAction(PRDAction prdAction){
        String actionType, entityName, propertyName;

        actionType = prdAction.getType().toUpperCase();
        entityName = prdAction.getEntity();
        propertyName = prdAction.getProperty();

        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            addErrorToList(prdAction, prdAction.getValue(), "Action type not allowed");
            ret = false;
        }

        if(type == ActionType.CONDITION){
            PRDCondition prdCondition = prdAction.getPRDCondition();
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
     * 'compareActionValueToGivenPropertyValue' helper for boolean actions/properties.
     */
    private boolean compareBooleanCaseForCondition(PRDCondition prdCondition){
        String actionType, entityName, propertyName;

        actionType = ActionType.CONDITION.toString();
        entityName = prdCondition.getEntity();
        propertyName = prdCondition.getProperty();

        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            addErrorToList(prdCondition, prdCondition.getValue(), "Action type not allowed");
            ret = false;
        }

        if(type == ActionType.CONDITION){
            if(prdCondition.getSingularity().equals("single") && (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))){
                addErrorToList(prdCondition, prdCondition.getValue(), "Condition operator type not allowed");
                ret = false;
            }
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("BOOLEAN"))){
            addErrorToList(prdCondition, prdCondition.getValue(), "The property value type doesn't match the action value type");
            ret = false;
        }

        return ret;
    }


    /**
     * 'compareActionValueToGivenPropertyValue' helper for String actions/properties.
     */
    private boolean compareStringCaseForAction(PRDAction prdAction){
        String actionType, entityName, propertyName;

        actionType = prdAction.getType().toUpperCase();
        entityName = prdAction.getEntity();
        propertyName = prdAction.getProperty();

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

    /**
     * 'compareActionValueToGivenPropertyValue' helper for String actions/properties.
     */
    private boolean compareStringCaseForCondition(PRDCondition prdCondition){
        String actionType, entityName, propertyName;

        actionType = ActionType.CONDITION.toString();
        entityName = prdCondition.getEntity();
        propertyName = prdCondition.getProperty();

        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            addErrorToList(prdCondition, prdCondition.getValue(), "Action type not allowed");
            ret = false;
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("STRING"))){
            addErrorToList(prdCondition, prdCondition.getValue(), "The property value type doesn't match the action value type");
            ret = false;
        }

        return ret;
    }
}
