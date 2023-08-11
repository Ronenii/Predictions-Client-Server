package engine2ui.simulation;

import engine2ui.simulation.objects.DTOEntity;
import engine2ui.simulation.properties.DTOEndingCondition;
import engine2ui.simulation.properties.DTORule;

import java.util.List;

public class SimulationData {
    private List<DTOEntity> entities;
    private List<DTORule> rules;
    private List<DTOEndingCondition> endingConditions;

    public SimulationData(List<DTOEntity> entities, List<DTORule> rules, List<DTOEndingCondition> endingConditions) {
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
    }

    public void addEntity(DTOEntity entity)
    {
        entities.add(entity);
    }

    public void addRule(DTORule rule)
    {
        rules.add(rule);
    }

    public void addRule(String name, boolean ticks, boolean probability, String... actions)
    {
        addRule(new DTORule(name, ticks, probability, actions));
    }

    public void addEndingCondition(DTOEndingCondition endingCondition)
    {
        endingConditions.add(endingCondition);
    }

    public void addEndingCondition(String type, int count)
    {
        endingConditions.add(new DTOEndingCondition(type, count));
    }
}
