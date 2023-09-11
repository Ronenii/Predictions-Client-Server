package simulation.objects.world;

import engine2ui.simulation.execution.SetResponse;
import engine2ui.simulation.execution.StartResponse;
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
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;

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

    private int totalPopulation;

    public World(Map<String, Property> environmentProperties, Map<String, Entity> entities, Map<String, Rule> rules, Map<EndingConditionType, EndingCondition> endingConditions, TicksCounter ticksCounter, Grid grid, int threadCount) {
        this.environmentProperties = environmentProperties;
        this.entities = entities;
        this.rules = rules;
        this.endingConditions = endingConditions;
        this.ticks = ticksCounter;
        this.timePassed = -1;
        this.threadCount = threadCount;
        this.grid = grid;
        totalPopulation = 0;
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
     *
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

    public void resetWorld() {
        ticks.resetTicks();
        this.timePassed = -1;
        for (Entity e : entities.values()
        ) {
            e.resetPopulation();
        }
        grid.populateGrid(getListOfAllInstances());
    }

    /**
     * This function is only used to build a list in order to populate the grid.
     *
     * @return A list of all entity instances across all entities.
     */
    private List<EntityInstance> getListOfAllInstances() {
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
            if (ticks.getTicks() >= endingConditions.get(EndingConditionType.TICKS).getCount()) {
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
            if (timePassed >= endingConditions.get(EndingConditionType.SECONDS).getCount()) {
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

    /**
     * Tries to set the entity's starting population according to the user's input.
     * Will do so only if the user is trying to set the population according to
     * @param input The entity input from the user.
     * @return A response with an appropriate message.
     */
    public SetResponse setEntityPopulation(EntityPopulationUserInput input) {
        int gridSize = grid.getColumns() * grid.getRows();
        if (entities.containsKey(input.getName())) {
            if (totalPopulation - entities.get(input.getName()).getStartingPopulation() + input.getPopulation() > gridSize) {
                return populationErrorMessage(gridSize);
            } else {
                totalPopulation -= entities.get(input.getName()).getStartingPopulation();
                entities.get(input.getName()).setStartingPopulation(input.getPopulation());
                totalPopulation += input.getPopulation();
                return new SetResponse(true, String.format("You have successfully changed %s's starting population to %s. You have %s more instances you can add.", entities.get(input.getName()).getName(), input.getPopulation(), gridSize - totalPopulation));
            }
        } else {
            if (totalPopulation + input.getPopulation() > gridSize) {
                return populationErrorMessage(gridSize);
            } else {
                totalPopulation += input.getPopulation();
                return new SetResponse(true, String.format("You have successfully set %s's starting population to %s. You have %s more instances you can add.", entities.get(input.getName()).getName(), input.getPopulation(), gridSize - totalPopulation));
            }
        }
    }

    private SetResponse populationErrorMessage(int gridSize) {
        return new SetResponse(false, String.format("ERROR: You are trying to set more entities than the grid size allows.\n" +
                "You can only add %s entity instances at most.\n" +
                "The maximum allowed number of entities is %s.", gridSize - totalPopulation, gridSize));
    }

    /**
     * In func we add the simulation to the thread-pool. If the simulation has no population
     * at all it will send back a response indicating the simulation was not added.
     */
    public StartResponse startSimulation(){
        int populationCount = 0;
        for (Entity e: entities.values()
             ) {
            populationCount += e.getStartingPopulation();
        }

        if(populationCount == 0){
            return new StartResponse(false, "ERROR: Could not start simulation. You need to have at least one entity with a population larger than 0.");
        }

        return new StartResponse(true, "Simulation was added to the queue successfully.");
    }
}
