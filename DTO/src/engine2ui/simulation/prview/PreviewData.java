package engine2ui.simulation.prview;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTOEndingCondition;
import engine2ui.simulation.genral.impl.properties.DTORule;

import java.util.List;

/**
 * A class that holds the simulation's data required to present in menu option 2.
 */
public class PreviewData{
    private final List<DTOEntity> entities;
    private final List<DTORule> rules;
    private final List<DTOEndingCondition> endingConditions;

    public PreviewData(List<DTOEntity> entities, List<DTORule> rules, List<DTOEndingCondition> endingConditions) {
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
    }

    public void addEntity(DTOEntity entity) {
        entities.add(entity);
    }

    public void addRule(DTORule rule) {
        rules.add(rule);
    }

    public void addRule(String name, int ticks, double probability, String... actions) {
        addRule(new DTORule(name, ticks, probability, actions));
    }

    public void addEndingCondition(DTOEndingCondition endingCondition) {
        endingConditions.add(endingCondition);
    }

    public void addEndingCondition(String type, int count) {
        endingConditions.add(new DTOEndingCondition(type, count));
    }
}
