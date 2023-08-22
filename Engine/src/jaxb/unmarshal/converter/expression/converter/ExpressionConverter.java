package jaxb.unmarshal.converter.expression.converter;

import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDCondition;
import simulation.objects.entity.Entity;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.PropertyValueExpression;
import simulation.properties.action.expression.impl.RegularValueExpression;
import simulation.properties.action.expression.impl.methods.EnvironmentExpression;
import simulation.properties.action.expression.impl.methods.RandomExpression;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

import java.util.Map;

/**
 * The 'ExpressionConverter' class convert PRDAction or PRDCondition into an Expression
 */

public class ExpressionConverter {
    private final Map<String, Property> environmentProperties;
    private final Map<String, Entity> entities;

    public ExpressionConverter(Map<String, Property> environmentProperties, Map<String, Entity> entities) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
    }

    /**
     * Invoke 'createExpressionObject' from PRDAction object
     */
    public Expression getExpressionObjectFromPRDAction(PRDAction prdAction) {
        Expression ret;
        PropertyType type = entities.get(prdAction.getEntity()).getProperties().get(prdAction.getProperty()).getType();

        if (prdAction.getType().equals("increase") || prdAction.getType().equals("decrease")){
            ret = createExpressionObject(prdAction.getBy(), type, prdAction.getEntity());
        }
        else {
            ret = createExpressionObject(prdAction.getValue(), type, prdAction.getEntity());
        }

        return ret;
    }

    /**
     * Invoke 'createExpressionObject' from PRDCondition object
     */
    public Expression getExpressionObjectFromPRDCondition(PRDCondition prdCondition) {
        PropertyType type = entities.get(prdCondition.getEntity()).getProperties().get(prdCondition.getProperty()).getType();
        return createExpressionObject(prdCondition.getValue(), type, prdCondition.getEntity());
    }

    /**
     * Create an Expression object according to the 'contextValue'.
     *
     * @param contextValue the given value (in String) from the XML
     * @param type the Action's property type
     * @param entityName the Action's entity name
     * @return the Action's Expression object.
     */
    public Expression createExpressionObject(String contextValue, PropertyType type, String entityName){
        Expression ret;

        ret = getExpressionIfFunction(contextValue);
        if(ret == null) {
            ret = getExpressionIfProperty(contextValue, entityName);
        }
        if(ret == null){
            ret = getExpressionByParseValue(contextValue,type);
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
    private Expression getExpressionIfFunction(String valueStr){
        String functionName = getFucntionName(valueStr), param1, param2;
        Expression ret = null;
        if(functionName != null) {
            switch (HelperFunctionsType.valueOf(functionName.toUpperCase())) {
                case ENVIRONMENT:
                    param1 = getFunctionParam(valueStr);
                    Property envVariable = environmentProperties.get(param1);
                    ret = new EnvironmentExpression(envVariable.getType(), envVariable);
                    break;
                case RANDOM:
                    param1 = getFunctionParam(valueStr);
                    ret = new RandomExpression(PropertyType.DECIMAL,Integer.parseInt(param1));
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
    private Expression getExpressionIfProperty(String valueStr, String entityName) {
        Entity entity = entities.get(entityName);
        Property property = entity.getProperties().get(valueStr);
        Expression ret = null;

        if(property != null){
            ret = new PropertyValueExpression(property.getType(), property);
        }

        return ret;
    }

    /**
     * Parse 'valueStr' according to the property type.
     */
    private Expression getExpressionByParseValue(String valueStr, PropertyType type){
        Expression ret = null;

        switch (type) {
            case DECIMAL:
                ret = new RegularValueExpression(PropertyType.DECIMAL, Integer.parseInt(valueStr));
                break;
            case FLOAT:
                ret = new RegularValueExpression(PropertyType.FLOAT, Double.parseDouble(valueStr));
                break;
            case BOOLEAN:
                if(valueStr.equals("true")){
                    ret = new RegularValueExpression(PropertyType.BOOLEAN,true);
                }
                else {
                    ret = new RegularValueExpression(PropertyType.BOOLEAN,false);;
                }
                break;
            case STRING:
                ret = new RegularValueExpression(PropertyType.STRING, valueStr);
                break;
        }

        return ret;
    }
}
