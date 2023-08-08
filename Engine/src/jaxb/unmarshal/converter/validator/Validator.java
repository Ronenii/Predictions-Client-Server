package jaxb.unmarshal.converter.validator;

import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDActivation;
import jaxb.schema.generated.PRDEntity;
import jaxb.schema.generated.PRDProperty;

public class Validator {

    private StringBuilder errorsList;


    private void validatePRDProperty(PRDProperty prdProperty) {
        validatePRDPropertyRange(prdProperty);
        validatePRDPropertyInitValue(prdProperty);
    }

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

    private void validatePRDPropertyInitValue(PRDProperty prdProperty) {
        if (!prdProperty.getPRDValue().isRandomInitialize() && prdProperty.getPRDValue().getInit().isEmpty()) {
            addErrorToList(prdProperty.getClass().getSimpleName(), prdProperty.getPRDName(), "A non random initialized property must contain an init value.");
        }
    }

    public void validatePRDEntity(PRDEntity prdEntity)
    {
        validatePRDEntityPopulation(prdEntity);
    }

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

    private void validatePRDActivationTicks(PRDActivation prdActivation) {
        int ticks = prdActivation.getTicks();

        if (ticks < 0) {
            // TODO: Add where the empty string is, the rule this activation belongs to.
            addErrorToList(prdActivation.getClass().getSimpleName(), "", "Ticks cannot be negative.");
        }
    }

    private void validatePRDActivationProbability(PRDActivation prdActivation) {
        double probability = prdActivation.getProbability();

        if (probability < 0 || probability > 1) {
            // TODO: Add where the empty string is, the rule this activation belongs to.
            addErrorToList(prdActivation.getClass().getSimpleName(), "", "Probability must be between 0 & 1.");
        }
    }

    public void validatePRDAction(PRDAction prdAction){
        //TODO: Implement this.
    }

    public void addErrorToList(String objectClass, String objectName, String error) {
        errorsList.append(String.format("In %s: %s, ", objectClass, objectName));
        errorsList.append(error);
        errorsList.append("\n");
    }

    public boolean containsErrors() {
        return errorsList.length() > 0;
    }
}
