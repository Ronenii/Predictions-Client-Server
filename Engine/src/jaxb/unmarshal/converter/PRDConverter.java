package jaxb.unmarshal.converter;

import jaxb.schema.generated.*;
import jaxb.unmarshal.converter.expression.converter.exception.InvalidBooleanValueException;
import jaxb.unmarshal.converter.expression.converter.exception.InvalidStringValueException;
import jaxb.unmarshal.converter.expression.converter.exception.ValueOutOfRangeException;
import jaxb.unmarshal.converter.expression.converter.exception.ExpressionConversionException;
import jaxb.unmarshal.converter.expression.converter.ExpressionAndValueValidator;
import jaxb.unmarshal.converter.validator.exception.PRDObjectConversionException;
import jaxb.unmarshal.converter.validator.PRDValidator;
import jaxb.unmarshal.converter.value.initializer.ValueInitializer;
import simulation.objects.entity.Entity;
import simulation.properties.ending.conditions.EndingConditionType;
import simulation.properties.rule.Rule;
import simulation.objects.world.World;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.ActionType;
import simulation.properties.action.impl.DecreaseAction;
import simulation.properties.action.impl.IncreaseAction;
import simulation.properties.action.impl.KillAction;
import simulation.properties.action.impl.SetAction;
import simulation.properties.action.impl.calculation.CalculationAction;
import simulation.properties.action.impl.calculation.ClaculationType;
import simulation.properties.action.impl.condition.AbstractConditionAction;
import simulation.properties.action.impl.condition.MultipleCondition;
import simulation.properties.action.impl.condition.SingleCondition;
import simulation.properties.action.impl.condition.ThenOrElse;
import simulation.properties.activition.Activation;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.impl.BooleanProperty;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.property.impl.StringProperty;

import java.util.*;

/**
 * A class used to convert the objects generated from the scheme to objects that will be used in the program.
 */
public class PRDConverter {
    // In order to get values from the world object on the run.

    private Map<String, Property> environmentProperties;
    private Map<String, Entity> entities;

    private Map<String, Rule> rules;

    private final PRDValidator validator;

    public PRDConverter() {
        validator = new PRDValidator();
        environmentProperties = new HashMap<>();
        entities = new HashMap<>();
        rules = new HashMap<>();
    }

    public World PRDWorld2World(PRDWorld prdWorld) {

        Set<EndingCondition> endingConditions;

        getEnvironmentPropertiesFromPRDWorld(prdWorld);

        getEntitiesFromPRDWorld(prdWorld);

        getRulesFromPRDWorld(prdWorld);

        endingConditions = getEndingConditions(prdWorld.getPRDTermination());

        if (validator.containsErrors()) {
            throw new IllegalArgumentException(validator.getErrorList());
        }

        return new World(environmentProperties, entities, rules, endingConditions);
    }

    /**
     * Extracts all valid environment properties from given PRDWorld.
     *
     * @param prdWorld the given PRDWorld to extract the EnvProperties from.
     * @return All Successfully converted environment properties
     */
    private void getEnvironmentPropertiesFromPRDWorld(PRDWorld prdWorld) {
        List<PRDEnvProperty> prdEnvProperties = prdWorld.getPRDEvironment().getPRDEnvProperty();
        Property propertyToAdd;

        for (PRDEnvProperty envProperty : prdEnvProperties) {
            propertyToAdd = PRDEnvProperty2Property(envProperty);
            if (propertyToAdd != null) {
                environmentProperties.put(envProperty.getPRDName(), propertyToAdd);
            }
        }
    }

    /**
     * Extracts all valid entities from given PRDWorld.
     *
     * @param prdWorld the given PRDWorld to extract the entities from.
     * @return All Successfully converted entities
     */
    private void getEntitiesFromPRDWorld(PRDWorld prdWorld) {
        List<PRDEntity> prdEntities = prdWorld.getPRDEntities().getPRDEntity();
        Entity entityToAdd;

        for (PRDEntity prdEntity : prdEntities) {
            entityToAdd = PRDEntity2Entity(prdEntity);
            if (entityToAdd != null) {
                entities.put(prdEntity.getName(), entityToAdd);
            }
        }
    }


    /**
     * Extracts all valid rule from given PRDWorld.
     *
     * @param prdWorld the given PRDWorld to extract the rule from.
     * @return All Successfully converted rules
     */
    private void getRulesFromPRDWorld(PRDWorld prdWorld) {
        List<PRDRule> prdRules = prdWorld.getPRDRules().getPRDRule();
        Rule ruleToAdd;

        for (PRDRule prdRule : prdRules) {
            ruleToAdd = PRDRule2Rule(prdRule);
            if (ruleToAdd != null) {
                rules.put(prdRule.getName(), ruleToAdd);
            }
        }
    }

    /**
     * Extracts all valid properties from given PRDEntity.
     *
     * @param prdEntity the given PRDEntity to extract the rule from.
     * @return All Successfully converted properties
     */
    private Map<String, Property> getPropertiesFromPRDEntity(PRDEntity prdEntity) {
        Map<String, Property> entityProperties = new HashMap<>();
        List<PRDProperty> prdEntityProperties = prdEntity.getPRDProperties().getPRDProperty();
        Property propertyToAdd;

        for (PRDProperty property : prdEntityProperties) {
            propertyToAdd = PRDProperty2Property(property, entityProperties);
            if (propertyToAdd != null) {
                entityProperties.put(property.getPRDName(), propertyToAdd);
            }
        }

        return entityProperties;
    }

    /**
     * Extracts all valid actions from given prdActions list.
     *
     * @param prdActions the given PRDAction list to extract the actions from.
     * @return All Successfully converted actions
     */
    private List<Action> getActionsFromPRDActionsList(List<PRDAction> prdActions) {
        List<Action> actions = new ArrayList<>();
        Action actionToAdd;

        for (PRDAction action : prdActions) {
            actionToAdd = PRDAction2Action(action);
            if (actionToAdd != null) {
                actions.add(actionToAdd);
            }
        }

        return actions;
    }

    /**
     * Converts the given PRDEnvProperty to Property
     *
     * @param prdEnvProperty the given PRDEnvProperty generated from reading the XML file
     * @return a Property representation of PRDEnvProperty.
     */
    private Property PRDEnvProperty2Property(PRDEnvProperty prdEnvProperty) {
        try {
            validator.validatePRDEnvProperty(prdEnvProperty, environmentProperties);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        Property ret = null;
        String name = prdEnvProperty.getPRDName();
        double to = prdEnvProperty.getPRDRange().getTo();
        double from = prdEnvProperty.getPRDRange().getFrom();

        try {
            switch (PropertyType.valueOf(prdEnvProperty.getType().toUpperCase())) {
                case DECIMAL:
                    ret = new IntProperty(name, (int) from, (int) to);
                    break;
                case FLOAT:
                    ret = new DoubleProperty(name, from, to);
                    break;
                case BOOLEAN:
                    ret = new BooleanProperty(name);
                    break;
                case STRING:
                    ret = new StringProperty(name);
                    break;
            }
        } catch (IllegalArgumentException e) {
            validator.addErrorToList(prdEnvProperty, prdEnvProperty.getPRDName(), String.format("%s is not a valid property type", prdEnvProperty.getType()));
            return null;
        }

        return ret;
    }


    /**
     * Converts the given PRDProperty to Property
     *
     * @param prdProperty the given prdProperty generated from reading the XML file
     * @return a Property representation of prdProperty.
     */
    private Property PRDProperty2Property(PRDProperty prdProperty, Map<String, Property> entityProperties) {
        try {
            validator.validatePRDProperty(prdProperty, entityProperties);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        Property ret = null;
        String name = prdProperty.getPRDName();

        Double to = null;
        Double from = null;

        if(prdProperty.getType().equals("decimal") || prdProperty.getType().equals("float")){
            to = prdProperty.getPRDRange().getTo();
            from = prdProperty.getPRDRange().getFrom();
        }

        boolean isRandomInit = prdProperty.getPRDValue().isRandomInitialize();
        String value = prdProperty.getPRDValue().getInit();
        PropertyType type = PropertyType.valueOf(prdProperty.getType().toUpperCase());

        try {
            switch (type) {
                case DECIMAL:
                    ret = new IntProperty(name, isRandomInit, parseOrRandomValue(type,value, isRandomInit, from, to) ,(int)from.doubleValue() , (int)to.doubleValue());
                    break;
                case FLOAT:
                    ret = new DoubleProperty(name, isRandomInit,parseOrRandomValue(type,value, isRandomInit, from, to), from, to);
                    break;
                case BOOLEAN:
                    ret = new BooleanProperty(name, isRandomInit,parseOrRandomValue(type,value, isRandomInit, from, to));
                    break;
                case STRING:
                    ret = new StringProperty(name, isRandomInit,parseOrRandomValue(type,value, isRandomInit, from, to));
                    break;
            }
        }
        catch (IllegalArgumentException e) {
            validator.addErrorToList(prdProperty, prdProperty.getPRDName(), "Invalid property type.");
        } catch (InvalidBooleanValueException e) {
            validator.addErrorToList(prdProperty, prdProperty.getPRDName(), "Invalid value type, value is not boolean.");
        } catch (InvalidStringValueException e) {
            validator.addErrorToList(prdProperty, prdProperty.getPRDName(), "Invalid value type, value is not composed of the pattern characters.");
        } catch (ValueOutOfRangeException e) {
            validator.addErrorToList(prdProperty, prdProperty.getPRDName(), "Invalid value type, the number is out of the given range.");
        }

        return ret;
    }

    private Object parseOrRandomValue(PropertyType type, String value, boolean isRandomInit, Double from, Double to) throws ValueOutOfRangeException, InvalidBooleanValueException, InvalidStringValueException {
        Object ret = null;

        switch (type) {
            case DECIMAL:
                ret = ValueInitializer.integerInitial(value, isRandomInit, (int)from.doubleValue(), (int)to.doubleValue());
                break;
            case FLOAT:
                ret = ValueInitializer.doubleInitial(value, isRandomInit, from, to);
                break;
            case BOOLEAN:
                ret = ValueInitializer.booleanInitial(value, isRandomInit);
                break;
            case STRING:
                ret = ValueInitializer.stringInitial(value, isRandomInit);
                break;
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
        try {
            validator.validatePRDEntity(prdEntity, entities);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        String name = prdEntity.getName();
        int population = prdEntity.getPRDPopulation();
        Map<String, Property> properties;

        properties = getPropertiesFromPRDEntity(prdEntity);

        return new Entity(population, name, properties);
    }

    /**
     * Converts the given PRDRule to Rule
     *
     * @param prdRule the given PRDRule generated from reading the XML file
     * @return a Rule representation of PRDRule.
     */
    private Rule PRDRule2Rule(PRDRule prdRule) {
        ExpressionAndValueValidator expressionAndValueValidator = new ExpressionAndValueValidator(environmentProperties,entities);

        try {
            validator.validatePRDRule(prdRule, entities, rules, expressionAndValueValidator);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        String name = prdRule.getName();
        Activation activation = PRDActivation2Activation(prdRule.getPRDActivation(), prdRule);
        List<Action> actions;

        actions = getActionsFromPRDActionsList(prdRule.getPRDActions().getPRDAction());

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

        try {
            switch (ActionType.valueOf(prdAction.getType().toUpperCase())) {
                case INCREASE:
                    ret = new IncreaseAction(prdAction.getProperty(), prdAction.getEntity(), prdAction.getBy());
                    break;
                case DECREASE:
                    ret = new DecreaseAction(prdAction.getProperty(), prdAction.getEntity(), prdAction.getBy());
                    break;
                case CALCULATION:
                    ret = getMulOrDiv(prdAction);
                    break;
                case CONDITION:
                    ret = getSingleOrMultiple(prdAction);
                    break;
                case SET:
                    ret = new SetAction(prdAction.getProperty(), prdAction.getEntity(), prdAction.getValue());
                    break;
                case KILL:
                    ret = new KillAction(prdAction.getProperty(), prdAction.getEntity(), null);
                    break;
                case REPLACE:
                    break;
                case PROXIMITY:
                    break;
            }
        } catch (IllegalArgumentException e) {
            validator.addErrorToList(prdAction, prdAction.getValue(), "Illegal action value.");
            return null;
        }

        return ret;
    }

    /**
     * Converts the given PRDAction to multiply or divide calculation action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return a CalculationAction representation of the given PRDActivation.
     */
    private CalculationAction getMulOrDiv(PRDAction prdAction) {
        CalculationAction ret = null;
        PRDMultiply mul = prdAction.getPRDMultiply();
        PRDDivide div = prdAction.getPRDDivide();

        // Without loss of generality, if mul equals null - the calculation action is not a multiply action.
        if (mul != null) {
            ret = new CalculationAction(prdAction.getProperty(), prdAction.getEntity(), mul.getArg1(), mul.getArg2(), ClaculationType.MULTIPLY, null);
        } else if (div != null) {
            ret = new CalculationAction(prdAction.getProperty(), prdAction.getEntity(), div.getArg1(), div.getArg2(), ClaculationType.DIVIDE, null);
        } else {
            validator.addErrorToList(prdAction, prdAction.getType(), "Calculation action is not Multiply or Divide");
        }

        return ret;
    }

    /**
     * Converts the given PRDAction to single or multiple condition action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return an AbstractConditionAction representation of the given PRDActivation.
     */
    private AbstractConditionAction getSingleOrMultiple(PRDAction prdAction) {
        AbstractConditionAction ret = null;
        PRDCondition prdCondition = prdAction.getPRDCondition();
        ThenOrElse thenActions, elseActions;
        // Then and else objects are created in this method.
        thenActions = getAndCreateThenOrElse(prdAction,true);
        elseActions = getAndCreateThenOrElse(prdAction,false);
        if (prdCondition.getSingularity().equals("single")) {
            ret = new SingleCondition(prdCondition.getProperty(), prdCondition.getEntity(), thenActions, elseActions, prdCondition.getOperator(), prdCondition.getValue());
        } else if (prdCondition.getSingularity().equals("multiple")) {
            ret = getMultipleConditionObject(prdCondition,thenActions,elseActions);
        }

        return ret;
    }

    private MultipleCondition getMultipleConditionObject(PRDCondition prdCondition, ThenOrElse thenActions, ThenOrElse elseActions){
        List<PRDCondition> prdSubConditions = prdCondition.getPRDCondition();
        List<AbstractConditionAction> objectSubConditions = new ArrayList<>();
        AbstractConditionAction conditionToAdd;

        for(PRDCondition prdSubCondition : prdSubConditions){
            conditionToAdd = getAbstractConditionToBuildMultiple(prdSubCondition);
            objectSubConditions.add(conditionToAdd);
        }

        return new MultipleCondition(prdCondition.getProperty(), prdCondition.getEntity(), thenActions, elseActions, prdCondition.getLogical(), objectSubConditions, null);
    }

    private AbstractConditionAction getAbstractConditionToBuildMultiple(PRDCondition prdCondition){
        AbstractConditionAction ret = null;

        if (prdCondition.getSingularity().equals("single")) {
            ret = new SingleCondition(prdCondition.getProperty(), prdCondition.getEntity(), null, null, prdCondition.getOperator(), prdCondition.getValue());
        } else if (prdCondition.getSingularity().equals("multiple")) {
            ret = getMultipleConditionObject(prdCondition, null, null);
        }
        return ret;
    }

    /**
     * Converts the given PRDAction to Then and Else objects which contain a set of actions to invoke.
     * According to the XML file, if one of them has no actions to invoke, the object remains null.
     *
     * @param prdAction   the given PRDAction generated from reading the XML file
     * @param thenOrElse  if true, the method creates an object from prdThen, otherwise from prdElse
     */
    private ThenOrElse getAndCreateThenOrElse(PRDAction prdAction, boolean thenOrElse) {
        // 'getThenOrElseActionSet' creates the Set of Actions for them both.
        // Because 'PRDThen' and 'PRDElse' are different objects, when we want to create the set for 'Then'
        // we send null for 'prdElse', same for Else.
        ThenOrElse ret = null;

        if(thenOrElse){
            List<Action> thenActionsSet = getThenOrElseActionSet(prdAction.getPRDThen(), null);
            if (thenActionsSet != null) {
                ret = new ThenOrElse(thenActionsSet);
            }
        }
        else {
            List<Action> elseActionsSet = getThenOrElseActionSet(null, prdAction.getPRDElse());
            if (elseActionsSet != null) {
                ret = new ThenOrElse(elseActionsSet);
            }
        }
       return ret;
    }

    /**
     * Converts the given PRDThen or PRDElse to The set of actions to invoke.
     * For example: if prdThen equals null, the method creates the set from the prdElse.
     *
     * @param prdThen the given PRDThen generated from reading the XML file
     * @param prdElse the given PRDElse generated from reading the XML file
     * @return a Set of actions representation of the given PRDThen or PRDElse.
     */
    private List<Action> getThenOrElseActionSet(PRDThen prdThen, PRDElse prdElse) {
        List<Action> ret = null;

        if (prdThen != null) {
            ret = getActionsFromPRDActionsList(prdThen.getPRDAction());
        } else if (prdElse != null) {
            ret = getActionsFromPRDActionsList(prdElse.getPRDAction());
        }

        return ret;
    }

    /**
     * Converts the given PRDActivation to Activation
     *
     * @param prdActivation the given PRDAction generated from reading the XML file
     * @return an Activation representation of PRDActivation.
     */
    private Activation PRDActivation2Activation(PRDActivation prdActivation, PRDRule prdRule) {
        Integer ticks = null;
        Double probability = null;

        if(prdActivation != null){
            ticks = prdActivation.getTicks();
            probability = prdActivation.getProbability();
        }

        if(ticks == null){
            ticks = 1;
        }

        if (probability == null){
            probability = 1.0;
        }

        return new Activation(ticks, probability);
    }

    /**
     * Extracts all valid properties from given PRDTermination.
     *
     * @param prdTermination The given List of termination conditions
     * @return a set of all valid ending conditions
     */
    private Set<EndingCondition> getEndingConditions(PRDTermination prdTermination) {
        try {
            validator.validatePRDTermination(prdTermination);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        Set<EndingCondition> endingConditions = new HashSet<>();

        for (Object endingConditionObj : prdTermination.getPRDByTicksOrPRDBySecond()) {
            if (endingConditionObj.getClass() == PRDByTicks.class) {
                endingConditions.add(PRDByTicks2EndingCondition((PRDByTicks) endingConditionObj));
            } else {
                endingConditions.add(PRDBySecond2EndingCondition((PRDBySecond) endingConditionObj));
            }
        }

        return endingConditions;
    }

    private EndingCondition PRDByTicks2EndingCondition(PRDByTicks prdByTicks) {
        return new EndingCondition(EndingConditionType.TICKS, prdByTicks.getCount());
    }

    private EndingCondition PRDBySecond2EndingCondition(PRDBySecond prdBySecond) {
        return new EndingCondition(EndingConditionType.TIME, prdBySecond.getCount());
    }
}
