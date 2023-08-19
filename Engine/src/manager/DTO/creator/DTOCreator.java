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
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.rule.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        DTOProperty[] dtoPropertiesArray;
        List<DTOProperty> dtoPropertyList = new ArrayList<>();

        entity.getProperties().forEach((key, value)->dtoPropertyList.add(getDTOProperty(value)));
        dtoPropertiesArray = dtoPropertyList.toArray(new DTOProperty[0]);
        return new DTOEntity(entity.getName(), entity.getStartingPopulation(), entity.getCurrentPopulation(), dtoPropertiesArray);
    }

    private DTOProperty getDTOProperty(Property property) {
        DTOProperty dtoProperty;

        if (property.getType() == PropertyType.DECIMAL) {
            IntProperty intProperty = (IntProperty)property;
            dtoProperty = new RangedDTOProperty(intProperty.getName(), intProperty.getType().toString(), intProperty.isRandInit(), intProperty.getFrom(), intProperty.getTo());
        } else if (property.getType() == PropertyType.FLOAT) {
            DoubleProperty doubleProperty = (DoubleProperty)property;
            dtoProperty = new RangedDTOProperty(doubleProperty.getName(), doubleProperty.getType().toString(), doubleProperty.isRandInit(), doubleProperty.getFrom(), doubleProperty.getTo());
        }
        else {
            dtoProperty = new NonRangedDTOProperty(property.getName(), property.getType().toString(), property.isRandInit());
        }

        return dtoProperty;
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
