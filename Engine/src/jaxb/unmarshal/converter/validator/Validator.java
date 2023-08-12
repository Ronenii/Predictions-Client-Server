package jaxb.unmarshal.converter.validator;

import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDActivation;
import jaxb.schema.generated.PRDEntity;
import jaxb.schema.generated.PRDProperty;
import objects.entity.Entity;
import properties.action.api.ActionType;
import properties.property.api.Property;

import java.util.Map;

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

    public void addErrorToList(String objectClass, String objectName, String error) {
        errorsList.append(String.format("In %s: %s, ", objectClass, objectName));
        errorsList.append(error);
        errorsList.append("\n");
    }

    public boolean containsErrors() {
        return errorsList.length() > 0;
    }
}
