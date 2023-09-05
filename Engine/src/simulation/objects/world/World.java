package simulation.objects.world;
import engine2ui.simulation.result.ResultData;
import manager.DTO.creator.DTOCreator;
import simulation.objects.entity.Entity;
import simulation.objects.entity.EntityInstance;
import simulation.objects.world.grid.Grid;
import simulation.objects.world.ticks.counter.TicksCounter;
import simulation.properties.ending.conditions.EndingConditionType;
import simulation.properties.rule.Rule;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.property.api.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class World implements Serializable {

    private final Map<String, Property> environmentProperties;
    private final Map<String, Entity> entities;
    private final Map<String, Rule> rules;
    private final Map<EndingConditionType, EndingCondition> endingConditions;
    private EndingCondition terminateCondition;
    private final TicksCounter ticks;
    private long timePassed;
    private long startingTime;
    private final int threadCount;
    private final Grid grid;

    public World(Map<String, Property> environmentProperties, Map<String, Entity> entities, Map<String, Rule> rules, Map<EndingConditionType, EndingCondition> endingConditions, TicksCounter ticksCounter, Grid grid, int threadCount) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
        this.ticks = ticksCounter;
        this.timePassed = -1;
        this.threadCount = threadCount;
        this.grid = grid;
    }

    public Map<String, Property> getEnvironmentProperties() {
        return environmentProperties;
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public Map<String, Rule> getRules() {
        return rules;
    }

    public Map<EndingConditionType, EndingCondition> getEndingConditions() {
        return endingConditions;
    }

    public EndingCondition getTerminateCondition() {
        return terminateCondition;
    }

    public TicksCounter getTicks() {
        return ticks;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getThreadCount() {
        return threadCount;
    }

    @Override
    public String toString() {
        StringBuilder worldToString = new StringBuilder("World{" +
                "environmentProperties=[");
        for (Property p : environmentProperties.values()) {
            int counter = 0;
            worldToString.append(p);
            if (++counter != environmentProperties.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", entities=[");
        for (Entity e : entities.values()) {
            int counter = 0;
            worldToString.append(e);
            if (++counter != entities.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", rules=[");
        for (Rule r : rules.values()) {
            int counter = 0;
            worldToString.append(r);
            if (++counter != rules.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append(", endingConditions=[");
        for (EndingCondition e : endingConditions.values()) {
            int counter = 0;
            worldToString.append(e);
            if (++counter != endingConditions.size())
                worldToString.append(", ");
            else
                worldToString.append(']');
        }

        worldToString.append('}');
        return worldToString.toString();
    }

    /**
     * While the ending conditions are not met, this function iterates over
     * all rules and tries to invoke their actions on each of the entities.
     * In the end, it converts the entities in this world to DTOs and
     * returns the array of DTO entities.
     * @return The result data of this simulation run.
     */
    public ResultData runSimulation() {
        grid.moveAllEntities();
        // Set the starting time to calculate later for 'ending by seconds'
        if (endingConditions.containsKey(EndingConditionType.SECONDS)) {
            startingTime = System.currentTimeMillis();
        }

        // Try to invoke all rules on all entities.
        do {
            for (Rule r : rules.values()
            ) {
                r.invokeRuleOnWorldEntities(entities.values(), ticks.getTicks());
            }
        } while ((!endingConditionsMet()));


        DTOCreator dtoCreator = new DTOCreator();
        return new ResultData(dtoCreator.convertEntities2DTOEntities(entities));
    }

    public void resetWorld(){
        ticks.resetTicks();
        this.timePassed = -1;
        for (Entity e: entities.values()
             ) {
            e.resetPopulation();
        }
        grid.populateGrid(getListOfAllInstances());
    }

    /**
     * This function is only used to build a list in order to populate the grid.
     * @return A list of all entity instances across all entities.
     */
    private List<EntityInstance> getListOfAllInstances(){
        List<EntityInstance> instances = new ArrayList<>();

        for (Entity e : entities.values()
             ) {
            instances.addAll(e.getEntityInstances());
        }

        return instances;
    }

    private boolean endingConditionsMet() {
        return isEndingBySecondsMet() || isEndingByTicksMet();
    }

    /**
     * @return If the simulation has met the required amount of ticks to stop it.
     */
    private boolean isEndingByTicksMet() {
        boolean ret = false;

        if (endingConditions.containsKey(EndingConditionType.TICKS)) {
            if (ticks.getTicks() >= endingConditions.get(EndingConditionType.TICKS).getCount()){
                terminateCondition = endingConditions.get(EndingConditionType.TICKS);
                ret = true;
            }
            ticks.incrementTick();
        }

        return ret;
    }

    /**
     * @return If the simulation has met the required amount of seconds to stop it.
     */
    private boolean isEndingBySecondsMet() {
        boolean ret = false;

        if (endingConditions.containsKey(EndingConditionType.SECONDS)) {
            timePassed = (System.currentTimeMillis() - startingTime) / 1000;
            if(timePassed >= endingConditions.get(EndingConditionType.SECONDS).getCount()){
                terminateCondition = endingConditions.get(EndingConditionType.SECONDS);
                ret = true;
            }
        }

        return ret;
    }

    @Override
    public int hashCode() {
        return environmentProperties.size() * entities.size() * rules.size() * endingConditions.size();
    }

    @Override
    public boolean equals(Object obj) {
        World toCompare = (World) obj;
        return (toCompare.endingConditions.equals(this.endingConditions)) && (toCompare.rules.equals(this.rules)) && (toCompare.entities.equals(entities)) && (toCompare.environmentProperties.equals(this.environmentProperties));
    }
}
