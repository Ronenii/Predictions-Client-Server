package jaxb.unmarshal.converter.validator;

import jaxb.schema.generated.*;
import jaxb.unmarshal.converter.api.Validator;
import jaxb.unmarshal.converter.expression.converter.ExpressionAndValueValidator;
import jaxb.unmarshal.converter.expression.converter.exception.ExpressionConversionException;
import jaxb.unmarshal.converter.validator.exception.PRDObjectConversionException;
import simulation.objects.entity.Entity;
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

    public void validatePRDProperty(PRDProperty prdProperty, Map<String, Property> entityProperties) throws PRDObjectConversionException {
        validatePRDPropertyDoesntExist(prdProperty, entityProperties);
        validatePRDPropertyRange(prdProperty);
        validatePRDPropertyInitValue(prdProperty);
        validatePRDPropertyValue(prdProperty);
    }

    private void validatePRDPropertyValue(PRDProperty prdProperty) throws PRDObjectConversionException {
        ExpressionAndValueValidator expressionAndValueValidator = new ExpressionAndValueValidator(null, null);

        if(!expressionAndValueValidator.isPRDPropertyValueMatchItsType(prdProperty)) {
            addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "The given property's value doesn't match the property type.");
        }
    }

    public void validatePRDEnvProperty(PRDEnvProperty prdEnvProperty, Map<String, Property> environmentProperties) throws PRDObjectConversionException {
        validatePRDEnvironmentPropertyDoesntExist(prdEnvProperty, environmentProperties);
        validatePRDEnvPropertyRange(prdEnvProperty);
    }

    private void validatePRDEnvironmentPropertyDoesntExist(PRDEnvProperty prdEnvProperty, Map<String, Property> environmentProperties) throws PRDObjectConversionException {
        String propertyName = prdEnvProperty.getPRDName();

        if(propertyName == null){
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
        if(prdEnvProperty.getType().equals("decimal") || prdEnvProperty.getType().equals("float"))
        {
            double from = prdEnvProperty.getPRDRange().getFrom();
            double to = prdEnvProperty.getPRDRange().getTo();

            if (from < 0.0 || to < 0.0) {
                addErrorToListAndThrowException(prdEnvProperty, prdEnvProperty.getPRDName(), "Range contains negative values.");
            }

            if (to <= from) {
                addErrorToListAndThrowException(prdEnvProperty, prdEnvProperty.getPRDName(), "Range value 'from' cannot be greater than or equal to 'to'.");
            }
        }
    }

    private void validatePRDPropertyDoesntExist(PRDProperty prdProperty, Map<String, Property> properties) throws PRDObjectConversionException {
        String propertyName = prdProperty.getPRDName();

        if(propertyName == null){
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
        if(prdProperty.getType().equals("decimal") || prdProperty.getType().equals("float")){
            double from = prdProperty.getPRDRange().getFrom();
            double to = prdProperty.getPRDRange().getTo();

            if (to <= from) {
                addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "Range value 'from' cannot be greater than or equal to 'to'.");
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
        validatePRDEntityPopulation(prdEntity);
    }

    /**
     * Validates that prdEntity's population is >= 0.
     *
     * @param prdEntity the PRDEntity we are validating
     */
    private void validatePRDEntityPopulation(PRDEntity prdEntity) throws PRDObjectConversionException {
        int population = prdEntity.getPRDPopulation();

        if (population < 0) {
            addErrorToListAndThrowException(prdEntity, prdEntity.getName(), "Population cannot be negative.");
        }
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
        if(prdRule.getName() == null){
            addErrorToListAndThrowException(prdRule, "Rule without name", "Property must have a name.");
        }

        if(rules.containsKey(prdRule.getName()))
        {
            addErrorToListAndThrowException(prdRule, prdRule.getName(), "There is already a rule with this name");
        }
    }

    /**
     * Validates that prdActivation's ticks is >= 0.
     *
     * @param prdActivation the PRDActivation we are validating
     */
    private void validatePRDActivationTicks(PRDActivation prdActivation, PRDRule prdRule) throws PRDObjectConversionException {
        if(prdActivation != null){
            Integer ticks = prdActivation.getTicks();

            if(ticks != null){
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
        if(prdActivation != null){
            Double probability = prdActivation.getProbability();

            if(probability != null){
                if (probability < 0 || probability > 1) {
                    addErrorToListAndThrowException(prdActivation, prdRule.getName(), "Probability must be between 0 & 1.");
                }
            }
        }
    }

    public void validatePRDAction(PRDAction prdAction, Map<String, Entity> entities, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {

        if(prdAction.getType().equals("calculation")){
            validatePRDActionEntityAndProperty(prdAction, prdAction.getResultProp(), entities, ruleName);
            validatePRDCalculation(prdAction, expressionAndValueValidator, ruleName);
        } else if (prdAction.getType().equals("condition")) {
            validatePRDCondition(prdAction.getPRDCondition(), entities, prdAction.getPRDThen(), prdAction.getPRDElse(), false, expressionAndValueValidator, ruleName);
        }
        else {
            validatePRDActionEntityAndProperty(prdAction, prdAction.getProperty(), entities, ruleName);
            validatePRDActionValue(prdAction, expressionAndValueValidator, ruleName);
        }
    }

    private void validatePRDActionValue(PRDAction prdAction, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        if (!prdAction.getType().equals("kill")){
            try{
                if(prdAction.getType().equals("increase") || prdAction.getType().equals("decrease")){
                    expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction,null, prdAction.getBy());
                }
                else {
                    expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction,null, prdAction.getValue());
                }
            } catch (ExpressionConversionException e) {
                addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, expressionAndValueValidator.getErrorMessage());
            }
        }
    }

    private void validatePRDConditionValue(PRDCondition prdCondition , ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        try{
            expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(null,prdCondition, prdCondition.getValue());
        } catch (ExpressionConversionException e) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, expressionAndValueValidator.getErrorMessage());
        }
    }


    private void validatePRDCalculation(PRDAction prdAction, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {

        if (prdAction.getType().equals("calculation")) {
            if (prdAction.getPRDMultiply() == null && prdAction.getPRDDivide() == null) {
                addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given calculation type does not contain multiply or divide actions.");
            }

            validatePRDCalculationValue(prdAction,expressionAndValueValidator, ruleName);
        }
    }

    private void validatePRDCalculationValue(PRDAction prdAction, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {
        PRDDivide prdDivide = prdAction.getPRDDivide();
        PRDMultiply prdMultiply = prdAction.getPRDMultiply();
        try {
            if(prdDivide != null){
                expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction,null, prdDivide.getArg1());
                expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction,null, prdDivide.getArg2());
            }
            else {
                expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction,null, prdMultiply.getArg1());
                expressionAndValueValidator.isPRDActionValueMatchItsPropertyType(prdAction,null, prdMultiply.getArg2());
            }
        }catch (ExpressionConversionException e) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, expressionAndValueValidator.getErrorMessage());
        }
    }


    private void validatePRDCondition(PRDCondition prdCondition, Map<String, Entity> entities, PRDThen prdThen, PRDElse prdElse, boolean isSubCondition, ExpressionAndValueValidator expressionAndValueValidator, String ruleName) throws PRDObjectConversionException {

        if (prdCondition == null) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given action type is 'condition' and does not contain 'condition' object.");
        }

        validatePRDConditionEntityAndProperty(prdCondition, entities, ruleName);

        if(prdCondition.getSingularity().equals("single")){
            validatePRDConditionValue(prdCondition, expressionAndValueValidator, ruleName);
        }

        if(prdCondition.getLogical() == null && prdCondition.getOperator() == null) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given condition type does not contain logical/operator.");
        }

        if (!prdCondition.getSingularity().equals("single") && !prdCondition.getSingularity().equals("multiple")) {
            addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given condition type does not contain singularity.");
        }


        if(prdCondition.getSingularity().equals("multiple")){
            if (prdCondition.getPRDCondition().size() < 2) {
                addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given multiple condition does not contain 2 sub conditions.");
            }

            for(PRDCondition subPRDCondition : prdCondition.getPRDCondition()) {
                actionNumber++;
                validatePRDCondition(subPRDCondition, entities, null, null, true, expressionAndValueValidator,ruleName);
            }
        }

        if(!isSubCondition) {
            if(prdThen == null){
                addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given condition type does not 'then' actions.");
            }
            for (PRDAction prdAction : prdThen.getPRDAction()){
                actionNumber++;
                validatePRDAction(prdAction,entities,expressionAndValueValidator,ruleName);
            }

            if (prdElse != null){
                for (PRDAction prdAction : prdElse.getPRDAction()){
                    actionNumber++;
                    validatePRDAction(prdAction,entities,expressionAndValueValidator,ruleName);
                }
            }
        }
    }

    /**
     * Property name is send to the method in order to use this method on calculation actions.
     */
    private void validatePRDActionEntityAndProperty(PRDAction prdAction, String propertyName, Map<String, Entity> entities, String ruleName) throws PRDObjectConversionException {
        String entityName = prdAction.getEntity();

        if(!prdAction.getType().equals("kill") && propertyName == null){
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given action's property doesn't exist.");
        }

        if (!entities.containsKey(entityName)) {
            addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given action's entity doesn't exist.");
        } else { // Can be done only if the given entity exists.
            Map<String, Property> properties = entities.get(entityName).getProperties();

            if (!prdAction.getType().equals("kill") && !properties.containsKey(propertyName)) {
                addActionErrorToListAndThrowException(ruleName, prdAction.getType(), actionNumber, "The given action's entity doesn't possess this given property.");
            }
        }
    }

    private void validatePRDConditionEntityAndProperty(PRDCondition prdCondition, Map<String, Entity> entities, String ruleName) throws PRDObjectConversionException {
        String entityName = prdCondition.getEntity(), propertyName;

        if(prdCondition.getSingularity().equals("single")){
            if (!entities.containsKey(entityName)) {
                addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given condition's entity doesn't exist.");
            } else { // Can be done only if the given entity exists.
                Map<String, Property> properties = entities.get(entityName).getProperties();
                propertyName = prdCondition.getProperty();

                if (!properties.containsKey(propertyName)) {
                    addActionErrorToListAndThrowException(ruleName, "condition", actionNumber, "The given action's entity doesn't possess this given property.");
                }
            }
        }
    }

    public void validatePRDTermination(PRDTermination prdTermination) throws PRDObjectConversionException {
        List<Object> byTicksOrSec = prdTermination.getPRDByTicksOrPRDBySecond();

        if (byTicksOrSec.isEmpty()) {
            addErrorToListAndThrowException(prdTermination, "", "There are no ending conditions for this simulation.");
        }
    }

    /**
     * Like the base method 'addErrorToList' but throws an exception as well.
     *
     * @throws PRDObjectConversionException
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
