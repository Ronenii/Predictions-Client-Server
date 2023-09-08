package engine2ui.simulation.prview;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTOEndingCondition;
import engine2ui.simulation.genral.impl.properties.DTOGridAndThread;
import engine2ui.simulation.genral.impl.properties.DTORule;
import engine2ui.simulation.genral.impl.properties.DTOEnvironmentVariable;

import java.util.List;

/**
 * A class that holds the simulation's data required to present in menu option 2.
 */
public class PreviewData{
    private final DTOGridAndThread gridAndThread;
    private final List<DTOEnvironmentVariable> envVariables;
    private final List<DTOEntity> entities;
    private final List<DTORule> rules;
    private final List<DTOEndingCondition> endingConditions;

    public PreviewData(DTOGridAndThread gridAndThread, List<DTOEnvironmentVariable> envVariables, List<DTOEntity> entities, List<DTORule> rules, List<DTOEndingCondition> endingConditions) {
        this.gridAndThread = gridAndThread;
        this.envVariables = envVariables;
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
    }

    public DTOGridAndThread getGridAndThread() {
        return gridAndThread;
    }

    public List<DTOEnvironmentVariable> getEnvVariables() {
        return envVariables;
    }

    public List<DTOEntity> getEntities() {
        return entities;
    }

    public List<DTORule> getRules() {
        return rules;
    }

    public List<DTOEndingCondition> getEndingConditions() {
        return endingConditions;
    }

    public void addEntity(DTOEntity entity) {
        entities.add(entity);
    }

    public void addRule(DTORule rule) {
        rules.add(rule);
    }


    public void addEndingCondition(DTOEndingCondition endingCondition) {
        endingConditions.add(endingCondition);
    }

    public void addEndingCondition(String type, int count) {
        endingConditions.add(new DTOEndingCondition(type, count));
    }
}
