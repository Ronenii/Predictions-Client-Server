package jaxb.unmarshal.converter.validator;

import jaxb.schema.generated.*;
import jaxb.unmarshal.converter.api.Validator;
import jaxb.unmarshal.converter.validator.exception.PRDObjectConversionException;
import simulation.objects.entity.Entity;
import simulation.properties.property.api.Property;

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

    private final StringBuilder errorsList;

    public PRDValidator() {
        this.errorsList = new StringBuilder();
    }

    public void validatePRDProperty(PRDProperty prdProperty, Map<String, Property> entityProperties) throws PRDObjectConversionException {
        validatePRDPropertyDoesntExist(prdProperty, entityProperties);
        validatePRDPropertyRange(prdProperty);
        validatePRDPropertyInitValue(prdProperty);
    }

    public void validatePRDEnvProperty(PRDEnvProperty prdEnvProperty, Map<String, Property> environmentProperties) throws PRDObjectConversionException {
        validatePRDEnvironmentPropertyDoesntExist(prdEnvProperty, environmentProperties);
        validatePRDEnvPropertyRange(prdEnvProperty);
    }

    private void validatePRDEnvironmentPropertyDoesntExist(PRDEnvProperty prdEnvProperty, Map<String, Property> environmentProperties) throws PRDObjectConversionException {
        String propertyName = prdEnvProperty.getPRDName();

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
        double from = prdEnvProperty.getPRDRange().getFrom();
        double to = prdEnvProperty.getPRDRange().getTo();

        if (from < 0 || to < 0) {
            addErrorToListAndThrowException(prdEnvProperty, prdEnvProperty.getPRDName(), "Range contains negative values.");
        }

        if (to <= from) {
            addErrorToListAndThrowException(prdEnvProperty, prdEnvProperty.getPRDName(), "Range value 'from' cannot be greater than or equal to 'to'.");
        }
    }

    private void validatePRDPropertyDoesntExist(PRDProperty prdProperty, Map<String, Property> properties) throws PRDObjectConversionException {
        String propertyName = prdProperty.getPRDName();

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
        double from = prdProperty.getPRDRange().getFrom();
        double to = prdProperty.getPRDRange().getTo();

        if (from < 0 || to < 0) {
            addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "Range contains negative values.");
        }

        if (to <= from) {
            addErrorToListAndThrowException(prdProperty, prdProperty.getPRDName(), "Range value 'from' cannot be greater than or equal to 'to'.");
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
        } else if (prdProperty.getPRDValue().isRandomInitialize() && !prdProperty.getPRDValue().getInit().isEmpty()) {
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

    public void validatePRDActivation(PRDActivation prdActivation) throws PRDObjectConversionException {
        validatePRDActivationTicks(prdActivation);
        validatePRDActivationProbability(prdActivation);
    }

    /**
     * Validates that prdActivation's ticks is >= 0.
     *
     * @param prdActivation the PRDActivation we are validating
     */
    private void validatePRDActivationTicks(PRDActivation prdActivation) throws PRDObjectConversionException {
        int ticks = prdActivation.getTicks();

        if (ticks < 0) {
            // TODO: Add where the empty string is, the rule this activation belongs to.
            addErrorToListAndThrowException(prdActivation, "", "Ticks cannot be negative.");
        }
    }

    /**
     * Validates that prdActivation's probability is >= 0 and <= 1.
     *
     * @param prdActivation the PRDActivation we are validating
     */
    private void validatePRDActivationProbability(PRDActivation prdActivation) throws PRDObjectConversionException {
        double probability = prdActivation.getProbability();

        if (probability < 0 || probability > 1) {
            // TODO: Add where the empty string is, the rule this activation belongs to.
            addErrorToListAndThrowException(prdActivation, "", "Probability must be between 0 & 1.");
        }
    }

    public void validatePRDAction(PRDAction prdAction, Map<String, Entity> entities) throws PRDObjectConversionException {
        validatePRDConditionAndPRDCalculation(prdAction);
        validatePRDActionEntityAndProperty(prdAction, entities);
    }

    private void validatePRDConditionAndPRDCalculation(PRDAction prdAction) throws PRDObjectConversionException {
        PRDCondition prdCondition;

        if (prdAction.getType().equals("calculation")) {
            if (prdAction.getPRDMultiply() == null && prdAction.getPRDDivide() == null) {
                addErrorToListAndThrowException(prdAction, "", "The given calculation type does not contain multiply or divide actions.");
            }
        }

        if (prdAction.getType().equals("condition")) {
            prdCondition = prdAction.getPRDCondition();
            if (prdCondition == null) {
                addErrorToListAndThrowException(prdAction, "", "The given action type is 'condition' and does not contain 'condition' object.");
            }

            if (!prdCondition.getSingularity().equals("single") && !prdCondition.getSingularity().equals("multiple")) {
                addErrorToListAndThrowException(prdAction, "", "The given condition type does not contain singularity.");
            }
        }
    }

    private void validatePRDActionEntityAndProperty(PRDAction prdAction, Map<String, Entity> entities) throws PRDObjectConversionException {
        String entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();

        if (!entities.containsKey(entityName)) {
            addErrorToListAndThrowException(prdAction, "", "The given action's entity doesn't exist.");
        } else { // Can be done only if the given entity exists.
            Map<String, Property> properties = entities.get(entityName).getProperties();
            if (!properties.containsKey(propertyName)) {
                addErrorToListAndThrowException(prdAction, "", "The given action's entity doesn't possess this given property.");
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
}
