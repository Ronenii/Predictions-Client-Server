package engine2ui.simulation.prview;

import engine2ui.simulation.genral.HasList;
import engine2ui.simulation.prview.objects.DTOEntity;
import engine2ui.simulation.prview.properties.DTOEndingCondition;
import engine2ui.simulation.prview.properties.DTORule;

import java.util.List;

/**
 * A class that holds the simulation's data required to present in menu option 2.
 */
public class PreviewData implements HasList{
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

    /**
     * @param list the list we wish to format
     * @param title the list's type but as a formatted string and not camel case
     * @param <T> the list's type.
     * @return the formatted list string as follows:
     *
     * ### 'TITLE' ###
     *
     * #1
     * list(0).toString()
     *
     * #2
     * list(1).toString()
     *
     * ...
     */
    private <T> String formatListWithTitle(List<T> list, String title){
        return String.format("### %s ###\n", title.toUpperCase()) + formatListToString(list);
    }


    @Override
    public String toString() {
        String ret = "";

        formatListWithTitle(entities, "ENTITIES");
        formatListWithTitle(rules, "RULES");
        formatListWithTitle(endingConditions, "ENDING CONDITIONS");

        return ret;
    }
}
