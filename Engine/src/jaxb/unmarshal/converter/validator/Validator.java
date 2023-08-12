package jaxb.unmarshal.converter.validator;

import jaxb.schema.generated.*;
import objects.entity.Entity;
import properties.property.api.Property;

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
public class Validator {

    private StringBuilder errorsList;

    public Validator() {
        this.errorsList = new StringBuilder();
    }

    private void validatePRDProperty(PRDProperty prdProperty) {
        validatePRDPropertyRange(prdProperty);
        validatePRDPropertyInitValue(prdProperty);
    }

    private void validatePRDPropertyExist(PRDProperty prdProperty, Map<String, Property> properties) {
        String propertyName = prdProperty.getPRDName();

        if(properties.containsKey(propertyName)) {
            addErrorToList(prdProperty.getClass().getSimpleName(), propertyName, "The given property already exists.");
        }
    }

     /**
     * Validates the range of prdProperty as follows:
     * 1) Checks that 'from' and 'to' are >= 0.
     * 2) Checks that 'from' is <= 'to'.
     *
     * @param prdProperty the PRDProperty we are validating
     */
    private void validatePRDPropertyRange(PRDProperty prdProperty) {
        double from = prdProperty.getPRDRange().getFrom();
        double to = prdProperty.getPRDRange().getTo();

        if (from < 0 || to < 0) {
            addErrorToList(prdProperty.getClass().getSimpleName(), prdProperty.getPRDName(), "Range contains negative values.");
        }

        if (to <= from) {
            addErrorToList(prdProperty.getClass().getSimpleName(), prdProperty.getPRDName(), "Range value 'from' cannot be greater than or equal to 'to'.");
        }
    }

    /**
     * Validates that if prdProperty is set to be randomly initialized, it won't get an init value from the user.
     * Also validates that if the prdProperty is not set to be randomly initialized, then it will receive an ini value from the user.
     *
     * @param prdProperty the PRDProperty we are validating
     */
    private void validatePRDPropertyInitValue(PRDProperty prdProperty) {
        if (!prdProperty.getPRDValue().isRandomInitialize() && prdProperty.getPRDValue().getInit().isEmpty()) {
            addErrorToList(prdProperty.getClass().getSimpleName(), prdProperty.getPRDName(), "A non random initialized property must contain an init value.");
        } else if (prdProperty.getPRDValue().isRandomInitialize() && !prdProperty.getPRDValue().getInit().isEmpty()) {
            addErrorToList(prdProperty.getClass().getSimpleName(), prdProperty.getPRDName(), "A random initialized property cannot contain an init value.");
        }
    }

    public void validatePRDEntity(PRDEntity prdEntity)
    {
        validatePRDEntityPopulation(prdEntity);
    }

    /**
     * Validates that prdEntity's population is >= 0.
     * @param prdEntity the PRDEntity we are validating
     */
    private void validatePRDEntityPopulation(PRDEntity prdEntity) {
        int population = prdEntity.getPRDPopulation();

        if (population < 0) {
            addErrorToList(prdEntity.getClass().getSimpleName(), prdEntity.getName(), "Population cannot be negative.");
        }
    }

    public void validatePRDActivation(PRDActivation prdActivation) {
        validatePRDActivationTicks(prdActivation);
        validatePRDActivationProbability(prdActivation);
    }

    /**
     * Validates that prdActivation's ticks is >= 0.
     * @param prdActivation the PRDActivation we are validating
     */
    private void validatePRDActivationTicks(PRDActivation prdActivation) {
        int ticks = prdActivation.getTicks();

        if (ticks < 0) {
            // TODO: Add where the empty string is, the rule this activation belongs to.
            addErrorToList(prdActivation.getClass().getSimpleName(), "", "Ticks cannot be negative.");
        }
    }

    /**
     * Validates that prdActivation's probability is >= 0 and <= 1.
     * @param prdActivation the PRDActivation we are validating
     */
    private void validatePRDActivationProbability(PRDActivation prdActivation) {
        double probability = prdActivation.getProbability();

        if (probability < 0 || probability > 1) {
            // TODO: Add where the empty string is, the rule this activation belongs to.
            addErrorToList(prdActivation.getClass().getSimpleName(), "", "Probability must be between 0 & 1.");
        }
    }

    public void validatePRDAction(PRDAction prdAction, Map<String, Entity> entities){
        validatePRDActionType(prdAction);
        validatePRDActionEntityAndProperty(prdAction, entities);

    }

    private void validatePRDActionType(PRDAction prdAction){
        ActionType type = ActionType.valueOf(prdAction.getType());

        if(type != ActionType.INCREASE &&
                type != ActionType.DECREASE &&
                type != ActionType.CALCULATION &&
                type != ActionType.CONDITION &&
                type != ActionType.KILL &&
                type != ActionType.SET) {
            addErrorToList(prdAction.getClass().getSimpleName(), "", "The given action's type doesn't exist.");
        }
    }

    private void validatePRDActionEntityAndProperty(PRDAction prdAction, Map<String, Entity> entities) {
        String entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();

        if(!entities.containsKey(entityName)) {
            addErrorToList(prdAction.getClass().getSimpleName(), "", "The given action's entity doesn't exist.");
        }
        else { // Can be done only if the given entity exists.
            Map<String, Property> properties = entities.get(entityName).getProperties();
            if(!properties.containsKey(propertyName)) {
                addErrorToList(prdAction.getClass().getSimpleName(), "", "The given action's entity doesn't possess this given property.");
            }
        }
    }

    public void validatePRDTermination(PRDTermination prdTermination) {
        List<Object> byTicksOrSec = prdTermination.getPRDByTicksOrPRDBySecond();

        if(byTicksOrSec.isEmpty()){
            addErrorToList(prdTermination.getClass().getSimpleName(), "", "There are no ending conditions for this simulation.");
        }
    }
  
    private void validatePRDActionEntityAndProperty(PRDAction prdAction, Map<String, Entity> entities) {
        String entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();

        if(!entities.containsKey(entityName)) {
            addErrorToList(prdAction.getClass().getSimpleName(), "", "The given action's entity doesn't exist.");
        }
        else { // Can be done only if the given entity exists.
            Map<String, Property> properties = entities.get(entityName).getProperties();
            if(!properties.containsKey(propertyName)) {
                addErrorToList(prdAction.getClass().getSimpleName(), "", "The given action's entity doesn't possess this given property.");
            }
        }
    }

    /**
     * Receives the given parameters, constructs and add the error message to the errorList string.
     *
     * @param objectClass the simple name of the class the error occurred in.
     * @param objectName  the name of the object the error occurred in
     * @param error       the error that occurred
     */
    public void addErrorToList(String objectClass, String objectName, String error) {
        errorsList.append(String.format("In %s: %s, ", objectClass, objectName));
        errorsList.append(error);
        errorsList.append("\n");
    }

    /**
     * @return returns true if the validator found any errors.
     */
    public boolean containsErrors() {
        return errorsList.length() > 0;
    }
}
