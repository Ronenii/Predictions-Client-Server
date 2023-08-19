package manager.DTO.creator;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTOEndingCondition;
import engine2ui.simulation.genral.impl.properties.DTORule;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.NonRangedDTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.RangedDTOProperty;
import engine2ui.simulation.prview.PreviewData;
import simulation.objects.entity.Entity;
import simulation.properties.action.api.Action;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.ending.conditions.EndingConditionType;
import simulation.properties.property.api.Property;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.rule.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Responsible cor converting program objects to DTO objects.
 */
public class DTOCreator {

    public PreviewData createSimulationPreviewDataObject(Map<String, Entity> entities, Map<String, Rule> rules, Map<EndingConditionType, EndingCondition> endingConditions) {
        List<DTOEntity> entitiesList;
        List<DTORule> rulesList;
        List<DTOEndingCondition> endingConditionsList;

        entitiesList = getDTOEntityList(entities);
        rulesList = getDTORulesList(rules);
        endingConditionsList = getDTOEndingConditionsList(endingConditions);
        return new PreviewData(entitiesList,rulesList,endingConditionsList);
    }

    private List<DTOEntity> getDTOEntityList(Map<String, Entity> entities) {
        List<DTOEntity> entitiesList = new ArrayList<>();

        entities.forEach((key, value)->entitiesList.add(getDTOEntity(value)));
        return entitiesList;
    }

    private DTOEntity getDTOEntity(Entity entity){
        DTOProperty[] dtoPropertiesArray = convertProperties2DTOProperties(entity.getProperties());
        return new DTOEntity(entity.getName(), entity.getStartingPopulation(), entity.getCurrentPopulation(), dtoPropertiesArray);
    }

    /**
     * Converts the given Map into an array of DTOProperties.
     * Used in menu option 4.
     * @param properties the properties we want to convert to an array.
     * @return an array of DTOProperties
     */
    private DTOProperty[] convertProperties2DTOProperties(Map<String, Property> properties) {
        Property[] propertyArr = properties.values().toArray(new Property[0]);
        DTOProperty[] dtoProperties = new DTOProperty[propertyArr.length];

        for (int i = 0; i < propertyArr.length; i++) {
            switch (propertyArr[i].getType()) {
                case DECIMAL:
                    IntProperty intProperty = (IntProperty) propertyArr[i];
                    dtoProperties[i] = new RangedDTOProperty(intProperty.getName(), intProperty.getType().toString(), intProperty.isRandInit(), intProperty.getFrom(), intProperty.getTo());
                    break;
                case FLOAT:
                    DoubleProperty doubleProperty = (DoubleProperty) propertyArr[i];
                    dtoProperties[i] = new RangedDTOProperty(doubleProperty.getName(), doubleProperty.getType().toString(), doubleProperty.isRandInit(), doubleProperty.getFrom(), doubleProperty.getTo());
                    break;
                case BOOLEAN:
                case STRING:
                    dtoProperties[i] = new NonRangedDTOProperty(propertyArr[i].getName(), propertyArr[i].getType().toString(), propertyArr[i].isRandInit());
                    break;
            }
        }
        return dtoProperties;
    }


    /**
     * Converts the given Map into an array of DTOEntities.
     * Used in menu option 4.
     * @param entities the entities we want to convert to an array.
     * @return an array of DTOEntities
     */
    public DTOEntity[] convertEntities2DTOEntities(Map<String, Entity> entities) {
        Entity[] entityArr = entities.values().toArray(new Entity[0]);
        DTOEntity[] dtoEntities = new DTOEntity[entityArr.length];

        for (int i = 0; i < entityArr.length; i++) {
            dtoEntities[i] = new DTOEntity(entityArr[i].getName(), entityArr[i].getStartingPopulation(), entityArr[i].getCurrentPopulation(), convertProperties2DTOProperties(entityArr[i].getProperties()));
        }

        return dtoEntities;
    }


    private List<DTORule> getDTORulesList(Map<String, Rule> rules) {
        List<DTORule> rulesList = new ArrayList<>();

        rules.forEach((key,value)->rulesList.add(getDTORule(value)));
        return rulesList;

    }

    private DTORule getDTORule(Rule rule) {
        List<String> actionsNames = new ArrayList<>();
        List<Action> actions = rule.getActions();
        String[] actionsNamesArray;

        actions.forEach((value)->actionsNames.add(value.getType().toString()));
        actionsNamesArray = actionsNames.toArray(new String[0]);
        return new DTORule(rule.getName(),rule.getActivation().getTicks(),rule.getActivation().getProbability(), actionsNamesArray);
    }

    private List<DTOEndingCondition> getDTOEndingConditionsList(Map<EndingConditionType, EndingCondition> endingConditions) {
        List<DTOEndingCondition> endingConditionsList = new ArrayList<>();

        endingConditions.forEach((key,value)->endingConditionsList.add(getDTOEndingCondition(value)));
        return endingConditionsList;
    }

    private DTOEndingCondition getDTOEndingCondition(EndingCondition endingCondition) {
        return new DTOEndingCondition(endingCondition.getType().toString(), endingCondition.getCount());
    }
}
