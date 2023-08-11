package jaxb.unmarshal.converter;

import jaxb.schema.generated.*;
import jaxb.unmarshal.converter.functions.HelperFunctionsType;
import jaxb.unmarshal.converter.functions.StaticHelperFunctions;
import jaxb.unmarshal.converter.validator.Validator;
import objects.entity.Entity;
import objects.rule.Rule;
import objects.world.World;
import properties.action.api.Action;
import properties.action.api.ActionType;
import properties.action.impl.DecreaseAction;
import properties.action.impl.IncreaseAction;
import properties.action.impl.KillAction;
import properties.action.impl.SetAction;
import properties.action.impl.calculation.CalculationAction;
import properties.action.impl.calculation.ClaculationType;
import properties.action.impl.condition.AbstractConditionAction;
import properties.action.impl.condition.MultipleCondition;
import properties.action.impl.condition.SingleCondition;
import properties.action.impl.condition.ThenOrElse;
import properties.activition.Activation;
import properties.ending.conditions.EndingCondition;
import properties.property.api.Property;
import properties.property.api.PropertyType;
import properties.property.impl.BooleanProperty;
import properties.property.impl.DoubleProperty;
import properties.property.impl.IntProperty;
import properties.property.impl.StringProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class used to convert the objects generated from the scheme to objects that will be used in the program.
 */
public class PRDConverter {
    // In order to get values from the world object on the run.

    private Map<String, Property> environmentProperties;
    private Map<String, Entity> entities;

    public World PRDWorld2World(PRDWorld prdWorld) {

        World newWorld;
        Map<String, Property> environmentProperties = new HashMap<>();
        Map<String, Entity> entities = new HashMap<>();
        Map<String, Rule> rules = new HashMap<>();
        Map<String, EndingCondition> endingConditions = new HashMap<>();

        // Iterates over all PRDEnvironmentProperties, converts each property and adds it to 'environmentProperties'
        prdWorld.getPRDEvironment().getPRDEnvProperty().forEach(p -> environmentProperties.put(p.getPRDName(), PRDEnvProperty2Property(p)));
        this.environmentProperties = environmentProperties;

        // Iterates over all PRDEntities, converts each entity and adds it to 'entities'
        prdWorld.getPRDEntities().getPRDEntity().forEach(e -> entities.put(e.getName(),PRDEntity2Entity(e)));
        this.entities = entities;

        // Iterates over all PRDRules, converts each rule and adds it to 'rules'
        prdWorld.getPRDRules().getPRDRule().forEach(r -> rules.put(r.getName(), PRDRule2Rule(r)));

        return new World(environmentProperties, entities, rules, endingConditions);
    }


    /**
     * Converts the given PRDEnvProperty to Property
     *
     * @param prdEnvProperty the given PRDEnvProperty generated from reading the XML file
     * @return a Property representation of PRDEnvProperty.
     * <p>
     * TODO: Right now I hard coded placeholders for thing such as @value & @is_Random_init in the ctors. We need to make this more elegant.
     */
    private Property PRDEnvProperty2Property(PRDEnvProperty prdEnvProperty) {
        Property ret = null;
        String name = prdEnvProperty.getPRDName();
        double to = prdEnvProperty.getPRDRange().getTo();
        double from = prdEnvProperty.getPRDRange().getFrom();

        switch (PropertyType.valueOf(prdEnvProperty.getType())) {
            case INT:
                ret = new IntProperty(name, false, 0, (int) from, (int) to);
                break;
            case DOUBLE:
                ret = new DoubleProperty(name, false, 0.0, from, to);
                break;
            case BOOLEAN:
                ret = new BooleanProperty(name, false, false);
                break;
            case STRING:
                ret = new StringProperty(name, false, "");
                break;
            default:
                String err = String.format("\"%s\" is not a valid Property type.", prdEnvProperty.getType());
                throw new IllegalArgumentException(err);
        }

        return ret;
    }


    /**
     * Converts the given PRDProperty to Property
     *
     * @param prdProperty the given prdProperty generated from reading the XML file
     * @return a Property representation of prdProperty.
     */
    private Property PRDProperty2Property(PRDProperty prdProperty) {
        Property ret = null;
        String name = prdProperty.getPRDName();

        double to = prdProperty.getPRDRange().getTo();
        double from = prdProperty.getPRDRange().getFrom();

        boolean isRandomInit = prdProperty.getPRDValue().isRandomInitialize();
        String value = prdProperty.getPRDValue().getInit();


        switch (PropertyType.valueOf(prdProperty.getType())) {
            case INT:
                ret = new IntProperty(name, isRandomInit, Integer.parseInt(value), (int) from, (int) to);
                break;
            case DOUBLE:
                ret = new DoubleProperty(name, isRandomInit, Double.parseDouble(value), from, to);
                break;
            case BOOLEAN:
                ret = new BooleanProperty(name, isRandomInit, Boolean.parseBoolean(value));
                break;
            case STRING:
                ret = new StringProperty(name, isRandomInit, value);
                break;
            default:
                String err = String.format("\"%s\" is not a valid Property type.", prdProperty.getType());
                throw new IllegalArgumentException(err);
        }

        return ret;
    }

    /**
     * Converts the given PRDEntity to Entity
     *
     * @param prdEntity the given PRDEntity generated from reading the XML file
     * @return an Entity representation of PRDEntity.
     */
    private Entity PRDEntity2Entity(PRDEntity prdEntity) {
        String name = prdEntity.getName();
        int population = prdEntity.getPRDPopulation();
        Map<String, Property> properties = new HashMap<>();

        // Iterates over all PRDProperties, converts each property and adds it to 'properties'
        prdEntity.getPRDProperties().getPRDProperty().forEach(c -> properties.put(c.getPRDName(), PRDProperty2Property(c)));

        return new Entity(population, name, properties);
    }

    /**
     * Converts the given PRDRule to Rule
     *
     * @param prdRule the given PRDRule generated from reading the XML file
     * @return a Rule representation of PRDRule.
     */
    private Rule PRDRule2Rule(PRDRule prdRule) {
        String name = prdRule.getName();
        Activation activation = PRDActivation2Activation(prdRule.getPRDActivation());
        Set<Action> actions = new HashSet<>();
        int counter = 0;

        // Iterates over all prdActions inside the prdRule and convert them to action
        prdRule.getPRDActions().getPRDAction().forEach(a -> {actions.add(PRDAction2Action(a));});

        return new Rule(name, activation, actions);
    }

    /**
     * Converts the given PRDAction to Action
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return an Action representation of PRDAction.
     */
    private Action PRDAction2Action(PRDAction prdAction) {
        Action ret = null;

        try{
            switch (ActionType.valueOf(prdAction.getType())) {
                case INCREASE:
                    ret = new IncreaseAction(prdAction.getProperty(),prdAction.getEntity(), analyzeAndGetValue(prdAction, prdAction.getBy()));
                case DECREASE:
                    ret = new DecreaseAction(prdAction.getProperty(),prdAction.getEntity(), analyzeAndGetValue(prdAction, prdAction.getBy()));
                case CALCULATION:
                    ret = getMulOrDiv(prdAction);
                case CONDITION:
                    ret = getSingleOrMultiple(prdAction);
                case SET:
                    ret = new SetAction(prdAction.getProperty(), prdAction.getEntity(), analyzeAndGetValue(prdAction, prdAction.getValue()));
                case KILL:
                    ret = new KillAction(prdAction.getProperty(), prdAction.getEntity());
                case REPLACE:
                    break;
                case PROXIMITY:
                    break;
                default:
                    String err = String.format("\"%s\" is not a valid Action type.", prdAction.getType());
                    throw new IllegalArgumentException(err);
            }
        }
        catch (Exception e){
            // Catch the exception from 'analyzeAndGetValue' in order to stop the current action creation.
        }
        return ret;
    }

    /**
     * Converts the given PRDAction to multiply or divide calculation action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return a CalculationAction representation of the given PRDActivation.
     */
    private CalculationAction getMulOrDiv(PRDAction prdAction){
        CalculationAction ret = null;
        PRDMultiply mul = prdAction.getPRDMultiply();
        PRDDivide div = prdAction.getPRDDivide();

        // Without loss of generality, if mul equals null - the calculation action is not a multiply action.
        if(mul != null){
            ret = new CalculationAction(prdAction.getProperty(),prdAction.getEntity(), analyzeAndGetValue(prdAction, mul.getArg1()), analyzeAndGetValue(prdAction, mul.getArg2()), ClaculationType.MULTIPLY);
        } else if (div != null) {
            ret = new CalculationAction(prdAction.getProperty(),prdAction.getEntity(), analyzeAndGetValue(prdAction, div.getArg1()), analyzeAndGetValue(prdAction, div.getArg2()), ClaculationType.DIVIDE);
        }
        else {
            // Throw exception.
        }
        return ret;
    }

    /**
     * Converts the given PRDAction to single or multiple condition action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return an AbstractConditionAction representation of the given PRDActivation.
     */
    private AbstractConditionAction getSingleOrMultiple(PRDAction prdAction){
        AbstractConditionAction ret = null;
        PRDCondition prdCondition = prdAction.getPRDCondition();
        ThenOrElse thenActions = null, elseActions = null;
        // Then and else objects are created in this method.
        getAndCreateThenOrElse(prdAction, thenActions, elseActions);

        if(prdCondition.getSingularity().equals("single")){
            ret = new SingleCondition(prdAction.getProperty(), prdAction.getEntity(), analyzeAndGetValue(prdAction, prdAction.getValue()), thenActions, elseActions, prdCondition.getOperator());
        } else if (prdCondition.getSingularity().equals("multiple")) {
            ret = new MultipleCondition(prdAction.getProperty(), prdAction.getEntity(), analyzeAndGetValue(prdAction, prdAction.getValue()), thenActions, elseActions, prdCondition.getLogical());
        }
        else {
            // Throw exception.
        }

        return ret;
    }

    /**
     * Converts the given PRDAction to Then and Else objects which contain a set of actions to invoke.
     * According to the XML file, if one of them has no actions to invoke, the object remains null.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @param thenActions empty ThenOrElse object to be created.
     * @param elseActions empty ThenOrElse object to be created.
     */
    private void getAndCreateThenOrElse(PRDAction prdAction, ThenOrElse thenActions, ThenOrElse elseActions){
        // 'getThenOrElseActionSet' creates the Set of Actions for them both.
        // Because 'PRDThen' and 'PRDElse' are different objects, when we want to create the set for 'Then'
        // we send null for 'prdElse', same for Else.
        Set<Action> thenActionsSet = getThenOrElseActionSet(prdAction.getPRDThen(), null);
        Set<Action> elseActionsSet = getThenOrElseActionSet(null, prdAction.getPRDElse());

        if(!thenActionsSet.isEmpty()){
            thenActions = new ThenOrElse(thenActionsSet);
        }

        if(!elseActionsSet.isEmpty()){
            elseActions = new ThenOrElse(elseActionsSet);
        }
    }

    /**
     * Converts the given PRDThen or PRDElse to The set of actions to invoke.
     * For example: if prdThen equals null, the method creates the set from the prdElse.
     *
     * @param prdThen the given PRDThen generated from reading the XML file
     * @param prdElse the given PRDElse generated from reading the XML file
     * @return a Set of actions representation of the given PRDThen or PRDElse.
     */
    private Set<Action> getThenOrElseActionSet(PRDThen prdThen, PRDElse prdElse){
        Set<Action> ret = new HashSet<>();

        if(prdThen != null){
            prdThen.getPRDAction().forEach(a-> ret.add(PRDAction2Action(a)));
        } else if (prdElse != null) {
            prdElse.getPRDAction().forEach(a-> ret.add(PRDAction2Action(a)));
        }

        return ret;
    }

    /**
     * Analyze the value string from the PRDAction, in case the given string represent a function,
     * the method extract the function name and parameters from the string, execute the function and return
     * the return value from this function.
     * Otherwise, the method returns the given string.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file
     * @param prdValueStr the given value name from the given PRDTAction generated from reading the XML file.
     * The name sent separately in order to analyze the two arguments of 'Calculation' action too.
     * @return the value requested object.
     */
    private Object analyzeAndGetValue(PRDAction prdAction, String prdValueStr){
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
            // Throw exception in order to stop the action creation.
        }

        return value;
    }

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

    private Property getIfProperty(PRDAction prdAction, String prdValueStr) {
        String entityName = prdAction.getEntity();
        Entity entity = entities.get(entityName);
        return entity.getProperties().get(prdValueStr);
    }

    private Object parseValue(String prdValueStr){
        boolean flag = false;
        Object ret = null;

        try{
            ret = Integer.parseInt(prdValueStr);
        }
        catch (NumberFormatException e){
            flag = true;
        }
        if(flag){
            ret = getBooleanOrStr(prdValueStr);
        }

        return ret;
    }

    private Object getBooleanOrStr(String prdValueStr){
        Object ret;
       try {
           ret = Boolean.valueOf(prdValueStr);
       }
       catch (IllegalArgumentException e){
           ret = prdValueStr;
       }

       return ret;
    }

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
        } else {
            // TODO: find a way to add this error to the error list in validator.
        }

        return ret;
    }

    private boolean compareIntegerOrDoubleCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type != ActionType.INCREASE && type != ActionType.DECREASE && type != ActionType.CALCULATION && type != ActionType.CONDITION){
            // TODO: find a way to add this error to the error list in validator.
            ret = false;
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("INT")) && (!propertyType.name().equals("DOUBLE"))){
            // TODO: find a way to add this error to the error list in validator.
            ret = false;
        }

        return ret;
    }

    private boolean compareBooleanCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        PRDCondition prdCondition;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION){
            // TODO: find a way to add this error to the error list in validator.
            ret = false;
        }

        if(type == ActionType.CONDITION){
            prdCondition = prdAction.getPRDCondition();
            if(prdCondition.getSingularity().equals("single") && (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))){
                // TODO: find a way to add this error to the error list in validator.
                ret = false;
            }
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("BOOLEAN"))){
            // TODO: find a way to add this error to the error list in validator.
            ret = false;
        }

        return ret;
    }

    private boolean compareStringCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION){
            // TODO: find a way to add this error to the error list in validator.
            ret = false;
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("STRING"))){
            // TODO: find a way to add this error to the error list in validator.
            ret = false;
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
     * Converts the given PRDActivation to Activation
     *
     * @param prdActivation the given PRDAction generated from reading the XML file
     * @return an Activation representation of PRDActivation.
     */
    private Activation PRDActivation2Activation(PRDActivation prdActivation) {
        int ticks = prdActivation.getTicks();
        double probability = prdActivation.getProbability();

        return new Activation(ticks, probability);
    }
}
