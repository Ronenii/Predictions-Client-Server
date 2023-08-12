package engine2ui.simulation.data;

import engine2ui.simulation.data.objects.DTOEntity;
import engine2ui.simulation.data.properties.DTOEndingCondition;
import engine2ui.simulation.data.properties.DTORule;

import java.util.List;

/**
 * A class that holds the simulation's data required to present in menu option 2.
 */
public class SimulationData {
    private List<DTOEntity> entities;
    private List<DTORule> rules;
    private List<DTOEndingCondition> endingConditions;

    public SimulationData(List<DTOEntity> entities, List<DTORule> rules, List<DTOEndingCondition> endingConditions) {
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


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        formatListToString(entities, "ENTITIES");
        formatListToString(rules, "RULES");
        formatListToString(endingConditions, "ENDING CONDITIONS");

        return ret.toString();
    }

    /**
     * @param list the list we wish to format
     * @param label the list's type but as a formatted string and not camel case
     * @param <T> the list's type.
     * @return the formatted list string as follows:
     *
     * ### 'LABEL' ###
     *
     * #1
     * list(0).toString()
     *
     * #2
     * list(1).toString()
     *
     * ...
     */
    private <T> String formatListToString(List<T> list, String label){
        StringBuilder ret = new StringBuilder();

        ret.append(String.format("### %s ###\n", label.toUpperCase()));
        for(int i =1; i < list.size(); i++){
            ret.append(String.format("\n#%s\n",i));
            ret.append(list.get(i-1));
        }

        return ret.toString();
    }
}
