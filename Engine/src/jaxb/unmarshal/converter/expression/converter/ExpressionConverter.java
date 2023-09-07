package jaxb.unmarshal.converter.expression.converter;

import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDCondition;
import jaxb.unmarshal.converter.expression.converter.two.params.TwoParams;
import simulation.objects.entity.Entity;
import simulation.objects.world.ticks.counter.TicksCounter;
import simulation.properties.action.expression.api.Expression;
import simulation.properties.action.expression.impl.PropertyValueExpression;
import simulation.properties.action.expression.impl.RegularValueExpression;
import simulation.properties.action.expression.impl.methods.*;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;

import java.util.Map;

/**
 * The 'ExpressionConverter' class convert PRDAction or PRDCondition into an Expression
 */

public class ExpressionConverter {
    private final Map<String, Property> environmentProperties;
    private final Map<String, Entity> entities;
    private final TicksCounter ticksCounter;

    public ExpressionConverter(Map<String, Property> environmentProperties, Map<String, Entity> entities, TicksCounter ticksCounter) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
        this.ticksCounter = ticksCounter;
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
    public Expression getExpressionObjectFromPRDCondition(PRDCondition prdCondition, PropertyType type) {
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

        ret = getExpressionIfFunction(contextValue, type, entityName);
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
    private Expression getExpressionIfFunction(String valueStr, PropertyType type, String entityName){
        String functionName = getFucntionName(valueStr), param;
        TwoParams twoParams;
        Expression ret = null;
        if(functionName != null) {
            param = getFunctionParam(valueStr);
            switch (HelperFunctionsType.valueOf(functionName.toUpperCase())) {
                case ENVIRONMENT:
                    Property envVariable = environmentProperties.get(param);
                    ret = new EnvironmentExpression(envVariable.getType(), envVariable);
                    break;
                case RANDOM:
                    ret = new RandomExpression(PropertyType.DECIMAL,Integer.parseInt(param));
                    break;
                case EVALUATE:
                    Property propertyForEvaluate = getProperty(param);
                    ret = new EvaluateExpression(propertyForEvaluate.getType(), propertyForEvaluate, getEntityName(param));
                    break;
                case PERCENT:
                    twoParams = getTwoParams(valueStr);
                    ret = new PercentExpression(type, createExpressionObject(twoParams.getParam1(),type,entityName), createExpressionObject(twoParams.getParam2(),type,entityName));
                    break;
                case TICKS:
                    Property propertyForTicks = getProperty(param);
                    ret = new TicksExpression(propertyForTicks.getType(), propertyForTicks, ticksCounter, getEntityName(param));
                    break;
            }
        }

        return ret;
    }

    /**
     * Receive string represent by this format: "<Entity>.<Property>", extract and return the entity's property if exists.
     */
    private Property getProperty(String valueStr) {
        String entityName, propertyName;
        int dotIndex = valueStr.indexOf(".");
        entityName = valueStr.substring(0,dotIndex);
        propertyName = valueStr.substring(dotIndex + 1, valueStr.length());
        return entities.get(entityName).getProperties().get(propertyName);
    }

    /**
     * Receive string represent by this format: "<Entity>.<Property>", extract and return the entity's name if exists.
     */
    private String getEntityName(String valueStr) {
        int dotIndex = valueStr.indexOf(".");

        return valueStr.substring(0,dotIndex);
    }

    /**
     * Receive string which represent a call to the side method "Percent", extract the two parameters from the string
     * and create a "TwoParams" object with these two params.
     */
    private TwoParams getTwoParams(String valueStr) {
        int openParenIndex = valueStr.indexOf("(");
        String argumentsStr = valueStr.substring(openParenIndex + 1, valueStr.length() - 1);
        String[] arguments = argumentsStr.split(",");
        return new TwoParams(arguments[0], arguments[1]);
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
                ret = new RegularValueExpression(PropertyType.DECIMAL, Integer.parseInt(valueStr), PropertyType.DECIMAL);
                break;
            case FLOAT:
                ret = new RegularValueExpression(PropertyType.FLOAT, Double.parseDouble(valueStr), PropertyType.FLOAT);
                break;
            case BOOLEAN:
                if(valueStr.equals("true")){
                    ret = new RegularValueExpression(PropertyType.BOOLEAN,true, PropertyType.BOOLEAN);
                }
                else {
                    ret = new RegularValueExpression(PropertyType.BOOLEAN,false, PropertyType.BOOLEAN);;
                }
                break;
            case STRING:
                ret = new RegularValueExpression(PropertyType.STRING, valueStr, PropertyType.FLOAT);
                break;
        }

        return ret;
    }
}
