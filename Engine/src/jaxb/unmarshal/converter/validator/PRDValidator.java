package jaxb.unmarshal.converter.validator;

import jaxb.schema.generated.*;
import jaxb.unmarshal.converter.api.Validator;
import jaxb.unmarshal.converter.expression.converter.ExpressionAndValueValidator;
import jaxb.unmarshal.converter.expression.converter.exception.ExpressionConversionException;
import jaxb.unmarshal.converter.validator.exception.PRDObjectConversionException;
import simulation.objects.entity.Entity;
import simulation.objects.world.definition.SimulationDefinition;
import simulation.properties.property.api.Property;
import simulation.properties.rule.Rule;

import java.util.List;
import java.util.Map;


/**
 * A class designated for validating PRD Objects created from the xml schema.
 * Only validates values provided by the user to the objects.
 * While trying to convert all the PRD Objects, we don't want to stop as soon as an error occurs,
 * We want to make a collection of all errors encountered during the conversion and then print this
 * collection to the user. This will allow the user to fix all of his errors at once instead of re-running
 * the program after each fix to try and find new errors.
 */
public class PRDValidator extends Validator {

    private int actionNumber;

    public void validatePRDGridAndPRDName(PRDWorld prdWorld, Map<String, SimulationDefinition> simulationDefinitions) throws PRDObjectConversionException {
        validatePRDGridSize(prdWorld.getPRDGrid());
        validatePRDName(prdWorld.getName(), simulationDefinitions);
    }

    private void validatePRDName(String worldName, Map<String, SimulationDefinition> simulationDefinitions) throws PRDObjectConversionException {
        if(simulationDefinitions.get(worldName) != null) {
            addErrorToListAndThrowException("prdWorld", "world", "The given world's name already exists");
        }
    }

    public void validatePRDGridSize(PRDWorld.PRDGrid prdGrid) throws PRDObjectConversionException {
        int rows = prdGrid.getRows(), columns = prdGrid.getColumns();
        if (rows < 10 || rows > 100 || columns < 10 || columns > 100) {
            addErrorToListAndThrowException(prdGrid,"grid", "The given world's grid rows/columns size must be between 10 to 100.");
        }
    }

    public void validatePRDProperty(PRDProperty prdProperty, Map<String, Property> entityProperties) throws PRDObjectConversionException {
        validatePRDPropertyDoesntExist(prdProperty, entityProperties);
        validatePRDPropertyRange(prdProperty);
        validatePRDPropertyInitValue(prdProperty);
        validatePRDPropertyValue(prdProperty);
    }

    private void validatePRDPropertyValue(PRDProperty prdProperty) throws PRDObjectConversionException {
        ExpressionAndValueValidator expressionAndValueValidator = new ExpressionAndValueValidator(null, null);

        if (!expressionAndValueValidator.isPRDPropertyValueMatchItsType(prdProperty)) {
            addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "The given property's value doesn't match the property type.");
        }
    }

    public void validatePRDEnvProperty(PRDEnvProperty prdEnvProperty, Map<String, Property> environmentProperties) throws PRDObjectConversionException {
        validatePRDEnvironmentPropertyDoesntExist(prdEnvProperty, environmentProperties);
        validatePRDEnvPropertyRange(prdEnvProperty);
    }

    private void validatePRDEnvironmentPropertyDoesntExist(PRDEnvProperty prdEnvProperty, Map<String, Property> environmentProperties) throws PRDObjectConversionException {
        String propertyName = prdEnvProperty.getPRDName();

        if (propertyName == null) {
            addErrorToListAndThrowException(prdEnvProperty, "Environment variable without name", "Property must have a name.");
        }


        if (environmentProperties.containsKey(propertyName)) {
            addErrorToListAndThrowException(prdEnvProperty, propertyName, "The given property already exists.");
        }
    }

    /**
     * Validates the range of prdProperty as follows:
     * 1) Checks that 'from' and 'to' are >= 0.
     * 2) Checks that 'from' is <= 'to'.
     *
     * @param prdEnvProperty the PRDProperty we are validating
     */
    private void validatePRDEnvPropertyRange(PRDEnvProperty prdEnvProperty) throws PRDObjectConversionException {
        if (prdEnvProperty.getType().equals("decimal") || prdEnvProperty.getType().equals("float")) {
            if(prdEnvProperty.getPRDRange() == null){
                addErrorToListAndThrowException(prdEnvProperty, prdEnvProperty.getPRDName(), "Environment variable type 'decimal' or 'float' must contain a range.");
            }

            double from = prdEnvProperty.getPRDRange().getFrom();
            double to = prdEnvProperty.getPRDRange().getTo();

            if (to <= from) {
                addErrorToListAndThrowException(prdEnvProperty, prdEnvProperty.getPRDName(), "Range value 'from' cannot be greater than or equal to 'to'.");
            }
        } else {
            if (prdEnvProperty.getPRDRange() != null) {
                addErrorToListAndThrowException(prdEnvProperty, prdEnvProperty.getPRDName(), "Boolean or string property can not contain range.");
            }
        }
    }

    private void validatePRDPropertyDoesntExist(PRDProperty prdProperty, Map<String, Property> properties) throws PRDObjectConversionException {
        String propertyName = prdProperty.getPRDName();

        if (propertyName == null) {
            addErrorToListAndThrowException(prdProperty, "Property without name", "Property must have a name.");
        }


        if (properties.containsKey(propertyName)) {
            addErrorToListAndThrowException(prdProperty, propertyName, "The given property already exists.");
        }
    }

    /**
     * Validates the range of prdProperty as follows:
     * 1) Checks that 'from' and 'to' are >= 0.
     * 2) Checks that 'from' is <= 'to'.
     *
     * @param prdProperty the PRDProperty we are validating
     */
    private void validatePRDPropertyRange(PRDProperty prdProperty) throws PRDObjectConversionException {
        if (prdProperty.getType().equals("decimal") || prdProperty.getType().equals("float")) {
            if(prdProperty.getPRDRange() == null){
                addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "Property type 'decimal' or 'float' must contain a range.");
            }

            double from = prdProperty.getPRDRange().getFrom();
            double to = prdProperty.getPRDRange().getTo();

            if (to <= from) {
                addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "Range value 'from' cannot be greater than or equal to 'to'.");
            }
        } else {
            if (prdProperty.getPRDRange() != null) {
                addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "Boolean or string property can not contain range.");
            }
        }

    }

    /**
     * Validates that if prdProperty is set to be randomly initialized, it won't get an init value from the user.
     * Also validates that if the prdProperty is not set to be randomly initialized, then it will receive an ini value from the user.
     *
     * @param prdProperty the PRDProperty we are validating
     */
    private void validatePRDPropertyInitValue(PRDProperty prdProperty) throws PRDObjectConversionException {
        if (!prdProperty.getPRDValue().isRandomInitialize() && prdProperty.getPRDValue().getInit().isEmpty()) {
            addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "A non random initialized property must contain an init value.");


        } else if (prdProperty.getPRDValue().isRandomInitialize() && prdProperty.getPRDValue().getInit() != null) {
            addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "A random initialized property cannot contain an init value.");
        }
    }

    public void validatePRDEntity(PRDEntity prdEntity, Map<String, Entity> entities) throws PRDObjectConversionException {
        validatePRDEntityDoesntExists(prdEntity, entities);
    }


    private void validatePRDEntityDoesntExists(PRDEntity prdEntity, Map<String, Entity> entities) throws PRDObjectConversionException {
        if (entities.containsKey(prdEntity.getName())) {
            addErrorToListAndThrowException(prdEntity, prdEntity.getName(), "There is already an entity with this name.");
        }
    }

    public void validatePRDActivation(PRDActivation prdActivation, PRDRule prdRule) throws PRDObjectConversionException {
        validatePRDActivationTicks(prdActivation, prdRule);
        validatePRDActivationProbability(prdActivation, prdRule);
    }

    public void validatePRDRule(PRDRule prdRule, Map<String, Entity> entities, Map<String, Rule> rules, ExpressionAndValueValidator expressionAndValueValidator) throws PRDObjectConversionException {
        validateRuleDoesntExist(prdRule, rules);
        validateAllRuleActions(prdRule, entities, expressionAndValueValidator);
        validatePRDActivation(prdRule.getPRDActivation(), prdRule);
    }

    private void validateAllRuleActions(PRDRule prdRule, Map<String, Entity> entities, ExpressionAndValueValidator expressionAndValueValidator) throws PRDObjectConversionException {
        actionNumber = 1;
        for (PRDAction a : prdRule.getPRDActions().getPRDAction()
        ) {
            validatePRDAction(a, entities, expressionAndValueValidator, prdRule.getName());
            actionNumber++;
        }
    }

    private void validateRuleDoesntExist(PRDRule prdRule, Map<String, Rule> rules) throws PRDObjectConversionException {
        if (prdRule.getName() == null) {
            addErrorToListAndThrowException(prdRule, "Rule without name", "Property must have a name.");
        }

        if (rules.containsKey(prdRule.getName())) {
            addErrorToListAndThrowException(prdRule, prdRule.getName(), "There is already a rule with this name");
        }
    }

    /**
     * Validates that prdActivation's ticks is >= 0.
     *
     * @param prdActivation the PRDActivation we are validating
     */
    private void validatePRDActivationTicks(PRDActivation prdActivation, PRDRule prdRule) throws PRDObjectConversionException {
        if (prdActivation != null) {
            Integer ticks = prdActivation.getTicks();

            if (ticks != null) {
                if (ticks < 0) {
                    addErrorToListAndThrowException(prdActivation, prdRule.getName(), "Ticks cannot be negative.");
                }
            }
        }
    }

    /**
     * Validates that prdActivation's probability is >= 0 and <= 1.
     *
     * @param prdActivation the PRDActivation we are validating
     */
    private void validatePRDActivationProbability(PRDActivation prdActivation, PRDRule prdRule) throws PRDObjectConversionException {
        if (prdActivation != null) {
            Double probability = prdActivation.getProbability();

            if (probability != null) {
                if (probability < 0 || probability > 1) {
                    addErrorToListAndThrowException(prdActivation, prdRule.getName(), "Probability must be between 0 & 1.");
                }
            }
        }
    }

    public void validatePRDAction(PRDAction prdAction, Map<String, Entity> entities, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        String propertyType;

        validatePRDSecondaryEntity(prdAction.getPRDSecondaryEntity(), entities, expressionAndValueValidator, ruleName);
        switch (prdAction.getType()) {
            case "calculation":
                propertyType = validatePRDActionEntityAndProperty(prdAction, prdAction.getResultProp(), entities, ruleName, expressionAndValueValidator);
                validatePRDCalculation(prdAction, expressionAndValueValidator, ruleName, propertyType);
                break;
            case "condition":
                validatePRDCondition(prdAction.getPRDCondition(), entities, prdAction.getPRDThen(), prdAction.getPRDElse(), false, expressionAndValueValidator, ruleName);
                break;
            case "replace":
                validatePRDReplace(prdAction, entities, ruleName);
                break;
            case "proximity":
                validatePRDProximity(prdAction, entities, ruleName, expressionAndValueValidator);
                break;
            default:
                propertyType = validatePRDActionEntityAndProperty(prdAction, prdAction.getProperty(), entities, ruleName, expressionAndValueValidator);
                validatePRDActionValue(prdAction, expressionAndValueValidator, ruleName, propertyType);
                break;
        }
    }

    private void validatePRDActionValue(PRDAction prdAction, ExpressionAndValueValidator expressionAndValueValidator, String ruleName, String propertyType) throws PRDObjectConversionException {
        if (!prdAction.getType().equals("kill")) {
            try {
                if (prdAction.getType().equals("increase") || prdAction.getType().equals("decrease")) {
                    expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction, null, prdAction.getBy(), propertyType);
                } else {
                    expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction, null, prdAction.getValue(), propertyType);
                }
            } catch (ExpressionConversionException e) {
                addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, String.format("For the value expression: %s",expressionAndValueValidator.getErrorMessage()));
            }
        }
    }

    /**
     * Validates prdSecondaryEntity's PRDSelection & that this secondaryEntity is defined.
     *
     * @param prdSecondaryEntity          The given secondary entity we want to validate,
     * @param entities                    A map of entities we use as context for selection's condition validation and to see that the secondary entity exists there.
     * @param expressionAndValueValidator Validates the expression inside the condition.
     * @param ruleName                    The rule in which this PRDSecondaryEntity's PRDAction is located
     * @throws PRDObjectConversionException Throws this Exception if selection validation failed or the secondary entity is not defined in entities.
     */
    private void validatePRDSecondaryEntity(PRDAction.PRDSecondaryEntity prdSecondaryEntity, Map<String, Entity> entities, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        if (prdSecondaryEntity != null) {
            validatePRDSelection(prdSecondaryEntity.getPRDSelection(), entities, expressionAndValueValidator, ruleName);
            if (!entities.containsKey(prdSecondaryEntity.getEntity())) {
                addErrorToListAndThrowException(prdSecondaryEntity, prdSecondaryEntity.getEntity(), "The given entity name does not exist. Only an existing entity can be defined as a secondary entity.");
            }
        }
    }


    /**
     * Validates that the given prdSelection has a valid condition and a count.
     *
     * @param prdSelection                the selection we validate.
     * @param entities                    A map of entities we use as context for the condition validation.
     * @param expressionAndValueValidator Validates the expression inside the condition.
     * @param ruleName                    The rule in which this PRDSelection's PRDAction is located
     * @throws PRDObjectConversionException Throws this Exception if the count or condition are invalid.
     */
    private void validatePRDSelection(PRDAction.PRDSecondaryEntity.PRDSelection prdSelection, Map<String, Entity> entities, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        validatePRDSelectionCount(prdSelection.getCount());
        validateSecondaryEntityPRDCondition(prdSelection.getPRDCondition(), entities, expressionAndValueValidator, ruleName);
    }

    /**
     * Validates that the given count in the PRDSelection is only "ALL" or a positive integer.
     *
     * @param count The number of secondary entities we want to access from the context entity.
     * @throws PRDObjectConversionException Throws this Exception if the count is invalid.
     */
    private void validatePRDSelectionCount(String count) throws PRDObjectConversionException {
        try {
            if (!count.equals("ALL")) {
                int toCheck = Integer.parseInt(count);
                if (toCheck < 1) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            addErrorToListAndThrowException(count, "Count", "Secondary entity count should be \"ALL\" or a positive integer.");
        }
    }

    private void validatePRDConditionValueAndProperty(PRDCondition prdCondition, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        String propertyType = null;
        try {
            propertyType = expressionAndValueValidator.isPropertyExpressionIsValid(prdCondition.getProperty(), prdCondition.getEntity(), true);

        } catch (ExpressionConversionException e) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, String.format("For the property expression: %s",expressionAndValueValidator.getErrorMessage()));
        }
        // Two try-catch blocks in order to indicate whether errors occurred in the property expression or in the value expression.
        try {
            expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(null, prdCondition, prdCondition.getValue(), propertyType);
        } catch (ExpressionConversionException e) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, String.format("For the value expression: %s",expressionAndValueValidator.getErrorMessage()));
        }
    }


    private void validatePRDCalculation(PRDAction prdAction, ExpressionAndValueValidator expressionAndValueValidator, String ruleName, String propertyType) throws PRDObjectConversionException {

        if (prdAction.getType().equals("calculation")) {
            if (prdAction.getPRDMultiply() == null && prdAction.getPRDDivide() == null) {
                addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given calculation type does not contain multiply or divide actions.");
            }

            validatePRDCalculationValue(prdAction, expressionAndValueValidator, ruleName, propertyType);
        }
    }

    private void validatePRDCalculationValue(PRDAction prdAction, ExpressionAndValueValidator expressionAndValueValidator, String ruleName, String propertyType) throws PRDObjectConversionException {
        PRDDivide prdDivide = prdAction.getPRDDivide();
        PRDMultiply prdMultiply = prdAction.getPRDMultiply();
        try {
            if (prdDivide != null) {
                expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction, null, prdDivide.getArg1(), propertyType);
                expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction, null, prdDivide.getArg2(), propertyType);
            } else {
                expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction, null, prdMultiply.getArg1(), propertyType);
                expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction, null, prdMultiply.getArg2(), propertyType);
            }
        } catch (ExpressionConversionException e) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, String.format("For the value expression: %s",expressionAndValueValidator.getErrorMessage()));
        }
    }


    private void validateSecondaryEntityPRDCondition(PRDCondition prdCondition, Map<String, Entity> entities, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        validateCommonPRDConditionValues(prdCondition, entities, expressionAndValueValidator, ruleName);

        if (prdCondition.getSingularity().equals("multiple")) {
            if (prdCondition.getPRDCondition().size() < 2) {
                addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given multiple condition does not contain 2 sub conditions.");
            }

            for (PRDCondition subPRDCondition : prdCondition.getPRDCondition()) {
                actionNumber++;
                validateSecondaryEntityPRDCondition(subPRDCondition, entities, expressionAndValueValidator, ruleName);
            }
        }
    }

    /**
     * This validation happens both in the secondary entity PRDCondition and in the regular prd condition.
     * Validates the following:
     * 1) The given entity and property do in fact exist and are legal to reference in this condition.
     * 2) The given value of the condition if it is a singularity.
     * 3) The logical and operator of this condition.
     * 4) That the condition is either a single condition or a multiple one.
     */
    private void validateCommonPRDConditionValues(PRDCondition prdCondition, Map<String, Entity> entities, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        validatePRDConditionEntity(prdCondition, entities, ruleName);

        if (prdCondition.getSingularity().equals("single")) {
            validatePRDConditionValueAndProperty(prdCondition, expressionAndValueValidator, ruleName);
        }

        if (prdCondition.getLogical() == null && prdCondition.getOperator() == null) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given condition type does not contain logical/operator.");
        }

        if (!prdCondition.getSingularity().equals("single") && !prdCondition.getSingularity().equals("multiple")) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given condition type does not contain singularity.");
        }
    }

    private void validatePRDCondition(PRDCondition prdCondition, Map<String, Entity> entities, PRDThen prdThen, PRDElse prdElse, boolean isSubCondition, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {

        if (prdCondition == null) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given action type is 'condition' and does not contain 'condition' object.");
        }

        validateCommonPRDConditionValues(prdCondition, entities, expressionAndValueValidator, ruleName);


        if (prdCondition.getSingularity().equals("multiple")) {
            if (prdCondition.getPRDCondition().size() < 2) {
                addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given multiple condition does not contain 2 sub conditions.");
            }

            for (PRDCondition subPRDCondition : prdCondition.getPRDCondition()) {
                actionNumber++;
                validatePRDCondition(subPRDCondition, entities, null, null, true, expressionAndValueValidator, ruleName);
            }
        }

        if (!isSubCondition) {
            if (prdThen == null) {
                addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given condition type does not 'then' actions.");
            }
            for (PRDAction prdAction : prdThen.getPRDAction()) {
                actionNumber++;
                validatePRDAction(prdAction, entities, expressionAndValueValidator, ruleName);
            }

            if (prdElse != null) {
                for (PRDAction prdAction : prdElse.getPRDAction()) {
                    actionNumber++;
                    validatePRDAction(prdAction, entities, expressionAndValueValidator, ruleName);
                }
            }
        }
    }

    private void validatePRDReplace(PRDAction prdAction, Map<String, Entity> entities, String ruleName) throws PRDObjectConversionException {
        if (prdAction.getKill() == null || prdAction.getCreate() == null) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given replace action does not contain kill/create entities.");
        }

        if (entities.get(prdAction.getKill()) == null || entities.get(prdAction.getCreate()) == null) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "One or two of the given replace action kill/create entities does not exist.");
        }

        if (prdAction.getMode() == null || (!prdAction.getMode().equals("scratch") && !prdAction.getMode().equals("derived"))) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given replace action does not contain valid mode");
        }
    }

    private void validatePRDProximity(PRDAction prdAction, Map<String, Entity> entities, String ruleName, ExpressionAndValueValidator expressionAndValueValidator) throws PRDObjectConversionException {
        if (prdAction.getPRDBetween() == null || prdAction.getPRDBetween().getSourceEntity() == null || prdAction.getPRDBetween().getTargetEntity() == null) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given proximity action does not contain valid 'Between'");
        }

        if (entities.get(prdAction.getPRDBetween().getSourceEntity()) == null || entities.get(prdAction.getPRDBetween().getTargetEntity()) == null) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "One or two of the given proximity action source/target entities does not exist.");
        }

        if (prdAction.getPRDEnvDepth() == null || prdAction.getPRDEnvDepth().getOf() == null) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given proximity action does not contain valid 'Env-Depth'");
        }

        try {
            expressionAndValueValidator.isPRDProximityDepthIsNumber(prdAction.getPRDEnvDepth().getOf(), prdAction.getPRDBetween().getSourceEntity());
        } catch (ExpressionConversionException e) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, expressionAndValueValidator.getErrorMessage());
        }

        if (prdAction.getPRDActions() != null && prdAction.getPRDActions().getPRDAction() != null) {
            for (PRDAction subPRDAction : prdAction.getPRDActions().getPRDAction()) {
                actionNumber++;
                validatePRDAction(subPRDAction, entities, expressionAndValueValidator, ruleName);
            }
        }
    }

    /**
     * Property name is send to the method in order to use this method on calculation actions, return the property type.
     */
    private String validatePRDActionEntityAndProperty(PRDAction prdAction, String propertyName, Map<String, Entity> entities, String ruleName, ExpressionAndValueValidator expressionAndValueValidator) throws PRDObjectConversionException {
        String entityName = prdAction.getEntity(), propertyType = null;

        if (!prdAction.getType().equals("kill") && propertyName == null) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given action's property doesn't exist.");
        }

        if (!entities.containsKey(entityName)) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given action's entity doesn't exist.");
        } else { // Can be done only if the given entity exists.
            if (!prdAction.getType().equals("kill")){
                try {
                    propertyType = expressionAndValueValidator.isPropertyExpressionIsValid(propertyName,entityName, false);
                } catch (ExpressionConversionException e) {
                    addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, String.format("For the property expression: %s",expressionAndValueValidator.getErrorMessage()));
                }
            }
        }

        return propertyType;
    }

    private void validatePRDConditionEntity(PRDCondition prdCondition, Map<String, Entity> entities, String ruleName) throws PRDObjectConversionException {
        String entityName = prdCondition.getEntity();

        if (prdCondition.getSingularity().equals("single")) {
            if (!entities.containsKey(entityName)) {
                addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given condition's entity doesn't exist.");
            }
        }
    }

    public void validatePRDTermination(PRDTermination prdTermination) throws PRDObjectConversionException {
        validatePRDTerminationNotEmpty(prdTermination);
        validateNumOfEndingConditions(prdTermination);
    }

    /**
     * This is validated since the program cannot run without some sort of ending condition
     * to stop it.
     */
    private void validatePRDTerminationNotEmpty(PRDTermination prdTermination) throws PRDObjectConversionException {
        List<Object> byTicksOrSec = prdTermination.getPRDBySecondOrPRDByTicks();

        if (byTicksOrSec.isEmpty() && prdTermination.getPRDByUser() == null) {
            addErrorToListAndThrowException(prdTermination, "", "There are no ending conditions for this simulation.");
        }
    }

    /**
     * We validate this since there cannot be more than one ending condition of each type.
     */
    private void validateNumOfEndingConditions(PRDTermination prdTermination) throws PRDObjectConversionException {
        int terminateByTicksCount = 0;
        int terminateBySecondsCount = 0;

        for (Object t : prdTermination.getPRDBySecondOrPRDByTicks()
        ) {
            if (t.getClass() == PRDByTicks.class) {
                terminateByTicksCount++;
            }
            if (t.getClass() == PRDBySecond.class) {
                terminateBySecondsCount++;
            }

            if (terminateByTicksCount > 1 || terminateBySecondsCount > 1) {
                addErrorToListAndThrowException(prdTermination, "", "There can only be at most one termination of each type.");
            }
        }

    }

    /**
     * Like the base method 'addErrorToList' but throws an exception as well.
     */
    public void addErrorToListAndThrowException(Object operatingClass, String objectName, String error) throws PRDObjectConversionException {
        super.addErrorToList(operatingClass, objectName, error);
        throw new PRDObjectConversionException();
    }

    public void addActionErrorToListAndThrowException(String ruleName, String actionName, int actionNumber, String error) throws PRDObjectConversionException {
        super.addActionErrorToList(ruleName, actionName, actionNumber, error);
        throw new PRDObjectConversionException();
    }
}
