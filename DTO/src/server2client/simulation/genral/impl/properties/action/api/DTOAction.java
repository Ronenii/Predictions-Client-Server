package server2client.simulation.genral.impl.properties.action.api;

import server2client.simulation.genral.impl.properties.action.impl.*;

public class DTOAction {
    private final DTOActionType actionType;
    private final String type;
    private final String mainEntity;
    private final String secondaryEntity;
    private final String property;
    private final String value;
    private DTOCalculation dtoCalculation;
    private DTOMultipleCondition dtoMultipleCondition;
    private DTOSingleCondition dtoSingleCondition;
    private DTOReplace dtoReplace;
    private DTOProximity dtoProximity;

    public DTOAction(DTOActionType actionType, String type, String mainEntity, String secondaryEntity, String property, String value) {
        this.actionType = actionType;
        this.type = type;
        this.mainEntity = mainEntity;
        this.secondaryEntity = secondaryEntity;
        this.property = property;
        this.value = value;
    }

    public DTOActionType getActionType() {
        return actionType;
    }

    public String getType() {
        return type;
    }

    public String getMainEntity() {
        return mainEntity;
    }

    public String getSecondaryEntity() {
        return secondaryEntity;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    public DTOCalculation getDtoCalculation() {
        return dtoCalculation;
    }

    public void setDtoCalculation(DTOCalculation dtoCalculation) {
        this.dtoCalculation = dtoCalculation;
    }

    public DTOMultipleCondition getDtoMultipleCondition() {
        return dtoMultipleCondition;
    }

    public void setDtoMultipleCondition(DTOMultipleCondition dtoMultipleCondition) {
        this.dtoMultipleCondition = dtoMultipleCondition;
    }

    public DTOSingleCondition getDtoSingleCondition() {
        return dtoSingleCondition;
    }

    public void setDtoSingleCondition(DTOSingleCondition dtoSingleCondition) {
        this.dtoSingleCondition = dtoSingleCondition;
    }

    public DTOReplace getDtoReplace() {
        return dtoReplace;
    }

    public void setDtoReplace(DTOReplace dtoReplace) {
        this.dtoReplace = dtoReplace;
    }

    public DTOProximity getDtoProximity() {
        return dtoProximity;
    }

    public void setDtoProximity(DTOProximity dtoProximity) {
        this.dtoProximity = dtoProximity;
    }
}
