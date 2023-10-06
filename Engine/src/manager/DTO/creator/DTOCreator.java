package manager.DTO.creator;

import server2client.simulation.genral.impl.objects.DTOEntity;
import server2client.simulation.genral.impl.objects.DTOEntityInstance;
import server2client.simulation.genral.impl.objects.DTOEntityPopulation;
import server2client.simulation.genral.impl.properties.DTOEndingCondition;
import server2client.simulation.genral.impl.properties.DTOGridAndThread;
import server2client.simulation.genral.impl.properties.DTORule;
import server2client.simulation.genral.impl.properties.action.api.DTOAction;
import server2client.simulation.genral.impl.properties.action.impl.*;
import server2client.simulation.genral.impl.properties.property.api.DTOProperty;
import server2client.simulation.genral.impl.properties.property.impl.NonRangedDTOProperty;
import server2client.simulation.genral.impl.properties.property.impl.RangedDTOProperty;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.genral.impl.properties.DTOEnvironmentVariable;
import simulation.objects.entity.Entity;
import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.properties.action.api.Action;
import simulation.properties.action.impl.DecreaseAction;
import simulation.properties.action.impl.IncreaseAction;
import simulation.properties.action.impl.KillAction;
import simulation.properties.action.impl.SetAction;
import simulation.properties.action.impl.calculation.CalculationAction;
import simulation.properties.action.impl.condition.MultipleCondition;
import simulation.properties.action.impl.condition.SingleCondition;
import simulation.properties.action.impl.proximity.ProximityAction;
import simulation.properties.action.impl.replace.ReplaceAction;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.ending.conditions.EndingConditionType;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.rule.Rule;

import java.util.*;

/**
 * Responsible cor converting program objects to DTO objects.
 */
public class DTOCreator {
    private static int actionNumber = 1;

    public PreviewData createSimulationPreviewDataObject(Map<String, Property> environmentProperties, Map<String, Entity> entities, Map<String, Rule> rules, Map<EndingConditionType, EndingCondition> endingConditions, Grid grid, int threadCount) {
        List<DTOEntity> entitiesList;
        List<DTORule> rulesList;
        List<DTOEndingCondition> endingConditionsList;
        List<DTOEnvironmentVariable> envVariables;
        DTOGridAndThread gridAndThread;

        envVariables = getDTOEnvironmentVariableList(environmentProperties);
        entitiesList = getDTOEntityList(entities);
        rulesList = getDTORulesList(rules);
        endingConditionsList = getDTOEndingConditionsList(endingConditions);
        gridAndThread = new DTOGridAndThread(grid.getRows(),grid.getColumns(),threadCount);
        return new PreviewData(gridAndThread, envVariables, entitiesList, rulesList, endingConditionsList);
    }

    public List<DTOEntityPopulation> getDTOEntityPopulationList(Map<String, Entity> entities) {
        List<DTOEntityPopulation> entityPopulationList = new ArrayList<>();

        for (Entity entity : entities.values()) {
            entityPopulationList.add(new DTOEntityPopulation(entity.getName(), entity.getCurrentPopulation()));
        }

        return entityPopulationList;
    }

    private List<DTOEnvironmentVariable> getDTOEnvironmentVariableList(Map<String, Property> environmentProperties){
        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>();
        Property valueFromTheMap;

        for (Map.Entry<String, Property> entry : environmentProperties.entrySet()) {
            valueFromTheMap = entry.getValue();
            environmentVariables.add(getDTOEnvironmentVariable(valueFromTheMap));
        }

        return environmentVariables;
    }

    /**
     * Create a 'DTOEnvironmentVariable' which contain the given environment variable's data and return it.
     */
    private DTOEnvironmentVariable getDTOEnvironmentVariable(Property valueFromTheMap) {
        String name = valueFromTheMap.getName(), type = valueFromTheMap.getType().toString().toLowerCase();
        Double from = null, to = null;

        if (valueFromTheMap.getType() == PropertyType.FLOAT) {
            DoubleProperty doubleProperty = (DoubleProperty) valueFromTheMap;
            from = doubleProperty.getFrom();
            to = doubleProperty.getTo();
        } else if (valueFromTheMap.getType() == PropertyType.DECIMAL) {
            IntProperty intProperty = (IntProperty) valueFromTheMap;
            from = (double) intProperty.getFrom();
            to = (double) intProperty.getTo();
        }

        return new DTOEnvironmentVariable(name, type, from, to);
    }

    public List<DTOEntity> getDTOEntityList(Map<String, Entity> entities) {
        List<DTOEntity> entitiesList = new ArrayList<>();

        entities.forEach((key, value) -> entitiesList.add(convertEntity2DTOEntity(value)));
        return entitiesList;
    }

    private DTOEntity convertEntity2DTOEntity(Entity entity) {
        DTOProperty[] dtoPropertiesArray = convertProperties2DTOPropertiesArr(entity.getProperties());
        DTOEntityInstance[] dtoEntityInstancesArray = convertEntityInstances2DTOEntityInstances(entity.getEntityInstances());
        return new DTOEntity(entity.getName(), entity.getStartingPopulation(), entity.getCurrentPopulation(), dtoPropertiesArray, dtoEntityInstancesArray);
    }

    /**
     * Converts the given Map into an array of DTOProperties.
     * Used in menu option 4.
     *
     * @param properties the properties we want to convert to an array.
     * @return an array of DTOProperties
     */
    private DTOProperty[] convertProperties2DTOPropertiesArr(Map<String, Property> properties) {
        Property[] propertyArr = properties.values().toArray(new Property[0]);
        DTOProperty[] dtoProperties = new DTOProperty[propertyArr.length];

        for (int i = 0; i < propertyArr.length; i++) {
            dtoProperties[i] = convertProperty2DTOProperty(propertyArr[i]);
        }

        return dtoProperties;
    }

    private DTOProperty convertProperty2DTOProperty(Property property) {
        DTOProperty ret = null;
        switch (property.getType()) {
            case DECIMAL:
                IntProperty intProperty = (IntProperty) property;
                ret = new RangedDTOProperty(intProperty.getName(), intProperty.getType().toString(), intProperty.isRandInit(), property.getValue(), property.getChangeTickAmount(), intProperty.getFrom(), intProperty.getTo());
                break;
            case FLOAT:
                DoubleProperty doubleProperty = (DoubleProperty) property;
                ret = new RangedDTOProperty(doubleProperty.getName(), doubleProperty.getType().toString(), doubleProperty.isRandInit(), property.getValue(),property.getChangeTickAmount(), doubleProperty.getFrom(), doubleProperty.getTo());
                break;
            case BOOLEAN:
            case STRING:
                ret = new NonRangedDTOProperty(property.getName(), property.getType().toString(), property.isRandInit(), property.getValue(), property.getChangeTickAmount());
                break;
        }
        return ret;
    }

    private DTOEntityInstance[] convertEntityInstances2DTOEntityInstances(List<EntityInstance> entityInstances) {
        int size = 0, entitiesAdded = 0;
        for (EntityInstance e : entityInstances
        ) {
            if (e.isAlive()) {
                size++;
            }
        }

        DTOEntityInstance[] dtoEntityInstances = new DTOEntityInstance[size];
        Map<String, DTOProperty> properties = new HashMap<>();

        for (EntityInstance toAdd : entityInstances) {
            if (toAdd.isAlive()) {
                dtoEntityInstances[entitiesAdded++] = new DTOEntityInstance(convertProperties2DTOPropertiesMap(toAdd.getProperties()));
            }
        }

        return dtoEntityInstances;
    }

    private Map<String, DTOProperty> convertProperties2DTOPropertiesMap(Map<String, Property> properties) {
        Map<String, DTOProperty> ret = new HashMap<>();

        for (Property p : properties.values()) {
            ret.put(p.getName(), convertProperty2DTOProperty(p));
        }

        return ret;
    }

    /**
     * Converts the given Map into an array of DTOEntities.
     * Used in menu option 4.
     *
     * @param entities the entities we want to convert to an array.
     * @return an array of DTOEntities
     */
    public DTOEntity[] convertEntities2DTOEntities(Map<String, Entity> entities) {
        Entity[] entityArr = entities.values().toArray(new Entity[0]);
        DTOEntity[] dtoEntities = new DTOEntity[entityArr.length];
        for (int i = 0; i < entityArr.length; i++) {
            DTOEntityInstance[] dtoEntityInstances = convertEntityInstances2DTOEntityInstances(entityArr[i].getEntityInstances());
            dtoEntities[i] = new DTOEntity(entityArr[i].getName(), entityArr[i].getStartingPopulation(), entityArr[i].getCurrentPopulation(), convertProperties2DTOPropertiesArr(entityArr[i].getProperties()), dtoEntityInstances);
        }

        return dtoEntities;
    }


    private List<DTORule> getDTORulesList(Map<String, Rule> rules) {
        List<DTORule> rulesList = new ArrayList<>();

        rules.forEach((key, value) -> rulesList.add(getDTORule(value)));
        return rulesList;

    }

    private DTORule getDTORule(Rule rule) {
        List<DTOAction> dtoActions = new ArrayList<>();
        List<Action> actions = rule.getActions();

        actions.forEach((value) -> dtoActions.add(getDTOAction(value)));
        actionNumber = 1;
        return new DTORule(rule.getName(), rule.getActivation().getTicks(), rule.getActivation().getProbability(), dtoActions);
    }

    private DTOAction getDTOAction(Action action){
        DTOAction ret = null;
        String type, mainEntity = action.getContextEntity(), secondaryEntity = null, property = null;

        type = String.format("%s #%d",action.getType().toString().toLowerCase(), actionNumber);
        actionNumber++;
        if(action.getContextProperty() != null){
            property = action.getContextProperty().toString();
        }
        if(action.getSecondaryEntity() != null){
            secondaryEntity = action.getSecondaryEntity().getContextEntity();
        }

        if(action instanceof IncreaseAction || action instanceof DecreaseAction){
            ret = new DTOIncreaseOrDecrease(type, mainEntity, secondaryEntity, property, action.getValueExpression().toString());
        } else if (action instanceof CalculationAction) {
            CalculationAction calculationAction = (CalculationAction)action;
            ret = new DTOCalculation(type, mainEntity, secondaryEntity, property, calculationAction.getArg1Expression().toString(), calculationAction.getArg2Expression().toString(), calculationAction.getCalculationType().toString().toLowerCase());
        } else if (action instanceof SingleCondition) {
            type = String.format("single condition #%d",actionNumber);
            SingleCondition singleCondition = (SingleCondition)action;
            ret = new DTOSingleCondition(type, mainEntity, secondaryEntity, property,singleCondition.getThenActionsCount(), singleCondition.getElseActionsCount(), singleCondition.getValueExpression().toString(), singleCondition.getOperator().toString().toLowerCase(), property);
        } else if (action instanceof MultipleCondition) {
            type = String.format("multiple condition #%d",actionNumber);
            MultipleCondition multipleCondition = (MultipleCondition)action;
            ret = new DTOMultipleCondition(type, mainEntity, secondaryEntity, property, multipleCondition.getThenActionsCount(),multipleCondition.getElseActionsCount(), multipleCondition.getLogical().toString().toLowerCase(), multipleCondition.getSubConditions().size());
        } else if (action instanceof SetAction) {
            ret = new DTOSet(type, mainEntity, secondaryEntity, property, action.getValueExpression().toString());
        } else if (action instanceof KillAction) {
            ret = new DTOKill(type, mainEntity, secondaryEntity, property);
        } else if (action instanceof ReplaceAction) {
            ReplaceAction replaceAction = (ReplaceAction)action;
            ret = new DTOReplace(type,mainEntity, secondaryEntity, property, replaceAction.getNewEntityName(), replaceAction.getReplaceType().toString().toLowerCase());
        } else if (action instanceof ProximityAction) {
            ProximityAction proximityAction = (ProximityAction)action;
            ret = new DTOProximity(type, mainEntity, secondaryEntity, property, proximityAction.getTargetEntityName(), proximityAction.getDepthString(), proximityAction.getSubActionsCount());
        }

        return ret;
    }

    private List<DTOEndingCondition> getDTOEndingConditionsList(Map<EndingConditionType, EndingCondition> endingConditions) {
        List<DTOEndingCondition> endingConditionsList = new ArrayList<>();

        endingConditions.forEach((key, value) -> endingConditionsList.add(getDTOEndingCondition(value)));
        return endingConditionsList;
    }

    private DTOEndingCondition getDTOEndingCondition(EndingCondition endingCondition) {
        return new DTOEndingCondition(endingCondition.getType().toString(), endingCondition.getCount());
    }
}
