package jaxb.unmarshal.converter;

import jaxb.schema.generated.*;
import jaxb.unmarshal.converter.expression.converter.exception.ExpressionConversionException;
import jaxb.unmarshal.converter.expression.converter.ExpressionConverterAndValidator;
import jaxb.unmarshal.converter.validator.exception.PRDObjectConversionException;
import jaxb.unmarshal.converter.validator.PRDValidator;
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
    }

    public World PRDWorld2World(PRDWorld prdWorld) {

        Set<EndingCondition> endingConditions;

        environmentProperties = getEnvironmentPropertiesFromPRDWorld(prdWorld);

        entities = getEntitiesFromPRDWorld(prdWorld);

        rules = getRulesFromPRDWorld(prdWorld);

        endingConditions = getEndingConditions(prdWorld.getPRDTermination());

        return new World(environmentProperties, entities, rules, endingConditions);
    }

    /**
     * Extracts all valid environment properties from given PRDWorld.
     *
     * @param prdWorld the given PRDWorld to extract the EnvProperties from.
     * @return All Successfully converted environment properties
     */
    private Map<String, Property> getEnvironmentPropertiesFromPRDWorld(PRDWorld prdWorld) {
        Map<String, Property> environmentProperties = new HashMap<>();
        List<PRDEnvProperty> prdEnvProperties = prdWorld.getPRDEvironment().getPRDEnvProperty();
        Property propertyToAdd;

        for (PRDEnvProperty envProperty : prdEnvProperties) {
            propertyToAdd = PRDEnvProperty2Property(envProperty, environmentProperties);
            if (propertyToAdd != null) {
                environmentProperties.put(envProperty.getPRDName(), propertyToAdd);
            }
        }

        return environmentProperties;
    }

    /**
     * Extracts all valid entities from given PRDWorld.
     *
     * @param prdWorld the given PRDWorld to extract the entities from.
     * @return All Successfully converted entities
     */
    private Map<String, Entity> getEntitiesFromPRDWorld(PRDWorld prdWorld) {
        Map<String, Entity> entities = new HashMap<>();
        List<PRDEntity> prdEntities = prdWorld.getPRDEntities().getPRDEntity();
        Entity entityToAdd;

        for (PRDEntity prdEntity : prdEntities) {
            entityToAdd = PRDEntity2Entity(prdEntity);
            if (entityToAdd != null) {
                entities.put(prdEntity.getName(), entityToAdd);
            }
        }

        return entities;
    }


    /**
     * Extracts all valid rule from given PRDWorld.
     *
     * @param prdWorld the given PRDWorld to extract the rule from.
     * @return All Successfully converted rules
     */
    private Map<String, Rule> getRulesFromPRDWorld(PRDWorld prdWorld) {
        Map<String, Rule> rules = new HashMap<>();
        List<PRDRule> prdRules = prdWorld.getPRDRules().getPRDRule();
        Rule ruleToAdd;

        for (PRDRule prdRule : prdRules) {
            ruleToAdd = PRDRule2Rule(prdRule);
            if (ruleToAdd != null) {
                rules.put(prdRule.getName(), ruleToAdd);
            }
        }

        return rules;
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

        if (validator.containsErrors()) {
            throw new IllegalArgumentException(validator.getErrorList());
        }
        return entityProperties;
    }

    /**
     * Extracts all valid actions from given prdActions list.
     *
     * @param prdActions the given PRDAction list to extract the actions from.
     * @return All Successfully converted actions
     */
    private Set<Action> getActionsFromPRDActionsList(List<PRDAction> prdActions) {
        Set<Action> actions = new HashSet<>();
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
    private Property PRDEnvProperty2Property(PRDEnvProperty prdEnvProperty, Map<String, Property> environmentProperties) {
        try {
            validator.validatePRDEnvProperty(prdEnvProperty, environmentProperties);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        Property ret = null;
        String name = prdEnvProperty.getPRDName();
        double to = prdEnvProperty.getPRDRange().getTo();
        double from = prdEnvProperty.getPRDRange().getFrom();

        //TODO: Change Enums to be like the given property types in the SML file
        switch (PropertyType.valueOf(prdEnvProperty.getType())) {
            case INT:
                ret = new IntProperty(name, (int) from, (int) to);
                break;
            case DOUBLE:
                ret = new DoubleProperty(name, from, to);
                break;
            case BOOLEAN:
                ret = new BooleanProperty(name);
                break;
            case STRING:
                ret = new StringProperty(name);
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
    private Property PRDProperty2Property(PRDProperty prdProperty, Map<String, Property> entityProperties) {
        try {
            validator.validatePRDProperty(prdProperty, entityProperties);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        Property ret = null;
        String name = prdProperty.getPRDName();

        double to = prdProperty.getPRDRange().getTo();
        double from = prdProperty.getPRDRange().getFrom();

        boolean isRandomInit = prdProperty.getPRDValue().isRandomInitialize();
        String value = prdProperty.getPRDValue().getInit();


        try {
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
            }
        } catch (IllegalArgumentException e) {
            validator.addErrorToList(prdProperty, prdProperty.getPRDName(), "Invalid property type.");
            return null;
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
        try {
            validator.validatePRDRule(prdRule, entities, rules);
        } catch (PRDObjectConversionException e) {
            return  null;
        }

        String name = prdRule.getName();
        Activation activation = PRDActivation2Activation(prdRule.getPRDActivation(), prdRule);
        Set<Action> actions;

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
        try {
            validator.validatePRDAction(prdAction, entities);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        Action ret = null;
        ExpressionConverterAndValidator expressionConverterAndValidator = new ExpressionConverterAndValidator(environmentProperties, entities);

        try {
            switch (ActionType.valueOf(prdAction.getType())) {
                case INCREASE:
                    ret = new IncreaseAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getBy()));
                case DECREASE:
                    ret = new DecreaseAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getBy()));
                case CALCULATION:
                    ret = getMulOrDiv(prdAction, expressionConverterAndValidator);
                case CONDITION:
                    ret = getSingleOrMultiple(prdAction, expressionConverterAndValidator);
                case SET:
                    ret = new SetAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getValue()));
                case KILL:
                    ret = new KillAction(prdAction.getProperty(), prdAction.getEntity());
                case REPLACE:
                    break;
                case PROXIMITY:
                    break;
            }
        } catch (IllegalArgumentException e) {
            validator.addErrorToList(prdAction, prdAction.getValue(), "Illegal action value.");
            return null;
        } catch (ExpressionConversionException e) {
            ret = null;
        }
        return ret;
    }

    /**
     * Converts the given PRDAction to multiply or divide calculation action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return a CalculationAction representation of the given PRDActivation.
     */
    private CalculationAction getMulOrDiv(PRDAction prdAction, ExpressionConverterAndValidator expressionConverterAndValidator) {
        CalculationAction ret = null;
        PRDMultiply mul = prdAction.getPRDMultiply();
        PRDDivide div = prdAction.getPRDDivide();

        // Without loss of generality, if mul equals null - the calculation action is not a multiply action.
        try {
            if (mul != null) {
                ret = new CalculationAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, mul.getArg1()),
                        expressionConverterAndValidator.analyzeAndGetValue(prdAction, mul.getArg2()), ClaculationType.MULTIPLY);
            } else if (div != null) {
                ret = new CalculationAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, div.getArg1()),
                        expressionConverterAndValidator.analyzeAndGetValue(prdAction, div.getArg2()), ClaculationType.DIVIDE);
            } else {
                validator.addErrorToList(prdAction, prdAction.getType(), "Calculation action is not Multiply or Divide");
                throw new ExpressionConversionException();
            }
        } catch (ExpressionConversionException e) {
            validator.addErrorToList(prdAction, prdAction.getType(), expressionConverterAndValidator.getErrorList());
        }
        return ret;
    }

    /**
     * Converts the given PRDAction to single or multiple condition action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return an AbstractConditionAction representation of the given PRDActivation.
     */
    private AbstractConditionAction getSingleOrMultiple(PRDAction prdAction, ExpressionConverterAndValidator expressionConverterAndValidator) {
        AbstractConditionAction ret = null;
        PRDCondition prdCondition = prdAction.getPRDCondition();
        ThenOrElse thenActions = null, elseActions = null;
        // Then and else objects are created in this method.
        getAndCreateThenOrElse(prdAction, thenActions, elseActions);

        try {
            if (prdCondition.getSingularity().equals("single")) {
                ret = new SingleCondition(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getValue()), thenActions, elseActions, prdCondition.getOperator());
            } else if (prdCondition.getSingularity().equals("multiple")) {
                ret = new MultipleCondition(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getValue()), thenActions, elseActions, prdCondition.getLogical());
            } else {
                validator.addErrorToList(prdAction, prdAction.getType(), "Condition action is not single or multiple.");
                throw new ExpressionConversionException();
            }
        } catch (ExpressionConversionException e) {
            validator.addErrorToList(prdAction, prdAction.getType(), expressionConverterAndValidator.getErrorList());
        }


        return ret;
    }

    /**
     * Converts the given PRDAction to Then and Else objects which contain a set of actions to invoke.
     * According to the XML file, if one of them has no actions to invoke, the object remains null.
     *
     * @param prdAction   the given PRDAction generated from reading the XML file
     * @param thenActions empty ThenOrElse object to be created.
     * @param elseActions empty ThenOrElse object to be created.
     */
    private void getAndCreateThenOrElse(PRDAction prdAction, ThenOrElse thenActions, ThenOrElse elseActions) {
        // 'getThenOrElseActionSet' creates the Set of Actions for them both.
        // Because 'PRDThen' and 'PRDElse' are different objects, when we want to create the set for 'Then'
        // we send null for 'prdElse', same for Else.
        Set<Action> thenActionsSet = getThenOrElseActionSet(prdAction.getPRDThen(), null);
        Set<Action> elseActionsSet = getThenOrElseActionSet(null, prdAction.getPRDElse());

        if (!thenActionsSet.isEmpty()) {
            thenActions = new ThenOrElse(thenActionsSet);
        }

        if (!elseActionsSet.isEmpty()) {
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
    private Set<Action> getThenOrElseActionSet(PRDThen prdThen, PRDElse prdElse) {
        Set<Action> ret = null;

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
        try {
            validator.validatePRDActivation(prdActivation, prdRule);
        } catch (PRDObjectConversionException e) {
            return null;
        }

        int ticks = prdActivation.getTicks();
        double probability = prdActivation.getProbability();

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
            return  null;
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
