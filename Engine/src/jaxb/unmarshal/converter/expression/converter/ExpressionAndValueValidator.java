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

public class ExpressionAndValueValidator {

    private final Map<String, Property> environmentProperties;
    private final Map<String, Entity> entities;

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public ExpressionAndValueValidator(Map<String, Property> environmentProperties, Map<String, Entity> entities) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
    }

    /**
     * Analyze the value string from the PRDProperty.
     * The method checks whether the object matches the property's value type.
     * If not, return false
     *
     * @param prdProperty the given PRDProperty generated from reading the XML file
     * @return return true if the value match the property type, otherwise return false.
     */
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
     * The method checks whether the object matches the property's value type and the action's type.
     * If not, an exception thrown in order to stop this action object creation.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file, if the purpose is to create the value from the prdCondition, this param will set to null
     * @param prdValueStr the given value name from the given PRDTAction generated from reading the XML file.
     * @param prdCondition the given PRDCondition generated from reading the XML file, if the purpose is to create the value from the prdAction, this param will set to null
     * The name sent separately in order to analyze the two arguments of 'Calculation' action too..
     */
    public void isPRDActionValueMatchItsPropertyType(PRDAction prdAction, PRDCondition prdCondition, String prdValueStr) throws ExpressionConversionException {
        String valueType;
        valueType = getObjectTypeIfFunction(prdValueStr);
        if(valueType == null){
            valueType = getTypeIfProperty(prdAction, prdCondition, prdValueStr);
        }
        if(valueType == null){
            valueType = parseValueType(prdValueStr);
        }

        compareActionValueToGivenPropertyValue(prdAction, prdCondition, valueType);
    }

    /**
     * Check if the PRDAction value is a function name and return the value type of the given function.
     * If the value is not a function name, the return value will be null.
     *
     * @param prdValueStr the PRDAction value string.
     * @return the return value type from the function if exists.
     */
    private String getObjectTypeIfFunction(String prdValueStr) throws ExpressionConversionException {
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
                errorMessage = "The value function's param doesn't match to the function.";
                throw new ExpressionConversionException();
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
     * @return the requested property value type
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
     * This method parse the string to one of the following types: Integer, Double, Boolean or String ant return the result type.
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
    private void compareActionValueToGivenPropertyValue(PRDAction prdAction, PRDCondition prdCondition, String valueType) throws ExpressionConversionException {
        PropertyType type = PropertyType.valueOf(valueType);

        if(type == PropertyType.DECIMAL || type == PropertyType.FLOAT){
            if(prdAction != null){
                compareIntegerOrDoubleCaseForAction(prdAction);
            }
            else {
                compareIntegerOrDoubleCaseForCondition(prdCondition);
            }
        }
        else {
            switch (PropertyType.valueOf(valueType)){
                case BOOLEAN:
                    if(prdAction != null){
                        compareBooleanCaseForAction(prdAction);
                    }
                    else {
                        compareBooleanCaseForCondition(prdCondition);
                    }

                    break;
                case STRING:
                    if (prdAction != null){
                        compareStringCaseForAction(prdAction);
                    }
                    else {
                        compareStringCaseForCondition(prdCondition);
                    }
                    break;
            }
        }
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for integer or double actions/properties.
     */
    private void compareIntegerOrDoubleCaseForAction(PRDAction prdAction) throws ExpressionConversionException {
        String actionType, entityName, propertyName;

        actionType = prdAction.getType().toUpperCase();
        entityName = prdAction.getEntity();
        if (actionType.equals("CALCULATION")){
            propertyName = prdAction.getResultProp();
        }
        else {
            propertyName = prdAction.getProperty();
        }

        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;

        if(type == ActionType.KILL)
        {
            errorMessage = "Action type not allowed";
            throw new ExpressionConversionException();
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("DECIMAL")) && (!propertyType.name().equals("FLOAT"))){
            errorMessage = "The property value type doesn't match the action value type";
            throw new ExpressionConversionException();
        }
    }


    /**
     * 'compareActionValueToGivenPropertyValue' helper for integer or double actions/properties.
     */
    private void compareIntegerOrDoubleCaseForCondition(PRDCondition prdCondition) throws ExpressionConversionException {
        String entityName, propertyName;

        entityName = prdCondition.getEntity();
        propertyName = prdCondition.getProperty();

        Entity entity = entities.get(entityName);
        PropertyType propertyType;

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("DECIMAL")) && (!propertyType.name().equals("FLOAT"))){
            errorMessage = "The property value type doesn't match the action value type";
            throw new ExpressionConversionException();
        }

    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for boolean actions/properties.
     */
    private void compareBooleanCaseForAction(PRDAction prdAction) throws ExpressionConversionException {
        String actionType, entityName, propertyName;

        actionType = prdAction.getType().toUpperCase();
        entityName = prdAction.getEntity();
        propertyName = prdAction.getProperty();

        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            errorMessage = "Action type not allowed";
            throw new ExpressionConversionException();
        }

        if(type == ActionType.CONDITION){
            PRDCondition prdCondition = prdAction.getPRDCondition();
            if(prdCondition.getSingularity().equals("single") && (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))){
                errorMessage =  "Boolean value can not compere with 'bt or 'lt'.";
                throw new ExpressionConversionException();
            }
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("BOOLEAN"))){
            errorMessage = "The property value type doesn't match the action value type";
            throw new ExpressionConversionException();
        }
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for boolean actions/properties.
     */
    private void compareBooleanCaseForCondition(PRDCondition prdCondition) throws ExpressionConversionException {
        String entityName, propertyName;

        entityName = prdCondition.getEntity();
        propertyName = prdCondition.getProperty();

        Entity entity = entities.get(entityName);
        PropertyType propertyType;

        if(prdCondition.getSingularity().equals("single") && (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))){
            errorMessage = "Boolean value can not compere with 'bt or 'lt'.";
            throw new ExpressionConversionException();
        }


        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("BOOLEAN"))){
            errorMessage = "The property value type doesn't match the action value type";
            throw new ExpressionConversionException();
        }
    }


    /**
     * 'compareActionValueToGivenPropertyValue' helper for String actions/properties.
     */
    private void compareStringCaseForAction(PRDAction prdAction) throws ExpressionConversionException {
        String actionType, entityName, propertyName;

        actionType = prdAction.getType().toUpperCase();
        entityName = prdAction.getEntity();
        propertyName = prdAction.getProperty();

        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            errorMessage = "Action type not allowed";
            throw new ExpressionConversionException();
        }

        if(type == ActionType.CONDITION){
            PRDCondition prdCondition = prdAction.getPRDCondition();
            if(prdCondition.getSingularity().equals("single") && (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))){
                errorMessage =  "String value can not compere with 'bt or 'lt'.";
                throw new ExpressionConversionException();
            }
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("STRING"))){
            errorMessage = "The property value type doesn't match the action value type";
            throw new ExpressionConversionException();
        }
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for String actions/properties.
     */
    private void compareStringCaseForCondition(PRDCondition prdCondition) throws ExpressionConversionException {
        String entityName, propertyName;

        entityName = prdCondition.getEntity();
        propertyName = prdCondition.getProperty();

        Entity entity = entities.get(entityName);
        PropertyType propertyType;

        if(prdCondition.getSingularity().equals("single") && (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))){
            errorMessage = "String value can not compere with 'bt or 'lt'.";
            throw new ExpressionConversionException();
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("STRING"))){
            errorMessage = "The property value type doesn't match the action value type";
            throw new ExpressionConversionException();
        }

    }
}
