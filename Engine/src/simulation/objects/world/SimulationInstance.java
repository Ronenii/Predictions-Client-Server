package simulation.objects.world;

import server2client.simulation.execution.SetResponse;
import server2client.simulation.runtime.ResultData;
import manager.DTO.creator.DTOCreator;
import simulation.objects.entity.Entity;
import simulation.objects.entity.EntityInstance;
import simulation.objects.world.exception.CrashException;
import simulation.objects.world.grid.Grid;
import simulation.objects.world.status.SimulationStatus;
import simulation.objects.world.ticks.counter.TicksCounter;
import simulation.objects.world.user.instructions.UserInstructions;
import simulation.properties.action.api.AbstractAction;
import simulation.properties.action.api.Action;
import simulation.properties.action.api.OneEntAction;
import simulation.properties.action.expression.impl.methods.TicksExpression;
import simulation.properties.action.impl.calculation.CalculationAction;
import simulation.properties.action.impl.condition.AbstractConditionAction;
import simulation.properties.action.impl.condition.MultipleCondition;
import simulation.properties.action.impl.proximity.ProximityAction;
import simulation.properties.action.impl.replace.ReplaceAction;
import simulation.properties.ending.conditions.EndingConditionType;
import simulation.properties.rule.Rule;
import simulation.properties.ending.conditions.EndingCondition;
import simulation.properties.property.api.Property;
import client2server.simulation.execution.user.input.EntityPopulationUserInput;

import java.io.Serializable;
import java.util.*;

public class SimulationInstance implements Serializable, Runnable {
    private String simulationId;
    private final String simulationName;
    private final Map<String, Property> environmentProperties;
    private final Map<String, Entity> entities;
    private final Map<String, Rule> rules;
    private Map<EndingConditionType, EndingCondition> endingConditions;
    private EndingCondition terminateCondition;
    private final TicksCounter ticksCounter;
    private long timePassed;
    private long timePassedBeforePause;
    private long startingTime;
    private int threadCount;
    private final Grid grid;
    private int totalPopulation;
    private final UserInstructions userInstructions;
    private final int constAll = -1;
    private SimulationStatus status;
    private ResultData resultData;
    private String errorMessage;

    public SimulationInstance(String simulationName, String simulationId, Map<String, Property> environmentProperties, Map<String, Entity> entities, Map<String, Rule> rules, TicksCounter ticksCounter, Grid grid) {
        this.simulationName = simulationName;
        this.simulationId = simulationId;
        this.environmentProperties = environmentProperties;
        this.entities = entities;
        this.rules = rules;
        this.ticksCounter = ticksCounter;
        this.timePassed = 0;
        this.grid = grid;
        totalPopulation = 0;
        this.status = SimulationStatus.WAITING;
        userInstructions = new UserInstructions(false, false, false);
    }

    public SimulationInstance(SimulationInstance simulationInstance) {
        this.simulationName = simulationInstance.getSimulationName();
        this.simulationId = simulationInstance.getSimulationId();
        this.environmentProperties = simulationInstance.dupEnvVarsMap();
        this.entities = simulationInstance.dupEntitiesMap();
        this.rules = simulationInstance.dupRules();
        this.endingConditions = simulationInstance.getEndingConditions();
        this.ticksCounter = new TicksCounter();
        this.timePassed = 0;
        this.threadCount = simulationInstance.threadCount;
        this.grid = new Grid(simulationInstance.grid);
        totalPopulation = 0;
        this.status = SimulationStatus.WAITING;
        userInstructions = new UserInstructions(false, false, false);
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(String simulationId) {
        this.simulationId = simulationId;
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

    public void setEndingConditions(Map<EndingConditionType, EndingCondition> endingConditions) {
        this.endingConditions = endingConditions;
    }

    public EndingCondition getTerminateCondition() {
        return terminateCondition;
    }

    public TicksCounter getTicksCounter() {
        return ticksCounter;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long getTimePassed() {
        if (userInstructions.isSimulationPaused) {
            return timePassedBeforePause;
        } else {
            return System.currentTimeMillis() - startingTime;
        }
    }

    public SimulationStatus getStatus() {
        return status;
    }

    public ResultData getResultData() {
        return resultData;
    }

    public UserInstructions getUserInstructions() {
        return userInstructions;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
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
     * The 3 following methods duplicate the environment variables, entities and rules maps.
     */
    public Map<String, Property> dupEnvVarsMap() {
        Map<String, Property> ret = new HashMap<>();

        for (Property property : environmentProperties.values()) {
            ret.put(property.getName(), property.dupProperty());
        }

        return ret;
    }

    public Map<String, Entity> dupEntitiesMap() {
        Map<String, Entity> ret = new HashMap<>();

        for (Entity entity : entities.values()) {
            ret.put(entity.getName(), entity.dupEntity());
        }

        return ret;
    }

    public Map<String, Rule> dupRules() {
        Map<String, Rule> ret = new LinkedHashMap<>();

        for (Rule rule : rules.values()) {
            ret.put(rule.getName(), rule.dupRule());
        }

        return ret;
    }

    @Override
    public void run() {
        runSimulation();
    }

    /**
     * While the ending conditions are not met, this function iterates over
     * all rules and tries to invoke their actions on each of the entities.
     * In the end, it converts the entities in this world to DTOs and
     * returns the array of DTO entities.
     *
     * @return The result data of this simulation run.
     */
    public void runSimulation() {
        resultData = new ResultData();
        DTOCreator dtoCreator = new DTOCreator();
        List<Action> actionsToInvoke = new ArrayList<>();

        initSimulation();
        // Simulation main loop
        do {
            //threadSleep();
            if (!userInstructions.isSimulationPaused || userInstructions.isSimulationSkippedForward) {
                try {
                    checkPopulation();
                    actionsToInvoke.clear();
                    grid.moveAllEntities();
                    // Get the invokable actions (by ticks and probability)
                    for (Rule r : rules.values()) {
                        actionsToInvoke.addAll(r.getActionsToInvoke());
                    }
                    // Invoke actions on the simulation entities.
                    for (Entity entity : entities.values()) {
                        invokeActionsOnAllInstances(entity.getEntityInstances(), actionsToInvoke);
                    }

                    setEntitiesPopulationRecord();
                    updateTickAndTime();
                } catch (CrashException e) {
                    errorMessage = e.getErrorMassage();
                    status = SimulationStatus.CRUSHED;
                }
                // create an entities DTO if the simulation moved by one tick.
                if (userInstructions.isSimulationSkippedForward) {
                    resultData.setEntities(dtoCreator.convertEntities2DTOEntities(entities));
                }

                userInstructions.isSimulationSkippedForward = false;
            }
        } while ((!endingConditionsMet()));

        if (status != SimulationStatus.CRUSHED) {
            resultData.setEntities(dtoCreator.convertEntities2DTOEntities(entities));
            this.status = SimulationStatus.COMPLETED;
        }
    }

    /**
     * Sets the result data entities population map for each entity.
     */
    private void setEntitiesPopulationRecord() {
        for (Entity e : entities.values()
        ) {
            resultData.setPopulationRecord(e.getName(), e.getCurrentPopulation());
        }
    }

    private void updateTickAndTime() {
        timePassed = System.currentTimeMillis() - startingTime;
        ticksCounter.incrementTick();
    }

    public void updateTimePassBeforePause() {
        timePassedBeforePause = timePassed;
    }

    public void resumeSimClock() {
        startingTime = System.currentTimeMillis() - timePassed;
    }

    private int calculateRemainingInstances() {
        int remainingInstances = 0;

        for (Entity e : entities.values()
        ) {
            remainingInstances += e.getCurrentPopulation();
        }
        return remainingInstances;
    }

    /**
     * Invoke the given actions on each entity instance of a specific entity.
     *
     * @param entityInstances A specific entity instances
     * @param actionsToInvoke List of invokable actions.
     */
    private void invokeActionsOnAllInstances(List<EntityInstance> entityInstances, List<Action> actionsToInvoke) throws CrashException {
        for (EntityInstance e : entityInstances) {
            if (e.isAlive()) {
                invokeActionsOnSingleInstance(e, actionsToInvoke);
            }
        }
    }

    /**
     * 'invokeActionsOnAllInstances' helper, invoke the list of action on a specific entity instance.
     */
    private void invokeActionsOnSingleInstance(EntityInstance entityInstance, List<Action> actionsToInvoke) throws CrashException {
        for (Action action : actionsToInvoke) {
            // Need to check again if an instance did not get kill in the previous actions.
            if (entityInstance.isAlive()) {
                if (action.getContextEntity().equals(entityInstance.getInstanceEntityName())) {
                    if (action.getSecondaryEntity() != null) {
                        invokeActionsWithSecondaryEntity(entityInstance, action);
                    } else {
                        invokeAnAction(entityInstance, action);
                    }
                }
            }
        }
    }

    private void invokeAnAction(EntityInstance entityInstance, Action action) throws CrashException {
        if (action.getClass().getSuperclass() == OneEntAction.class) {
            ((OneEntAction) action).invoke(entityInstance, false, ticksCounter.getTicks());
        } else if (action instanceof CalculationAction) {
            CalculationAction calculationAction = (CalculationAction) action;
            calculationAction.invoke(entityInstance, false, false, ticksCounter.getTicks());
        } else if (action instanceof AbstractConditionAction) {
            AbstractConditionAction abstractConditionAction = (AbstractConditionAction) action;
            abstractConditionAction.invoke(entityInstance, grid, ticksCounter.getTicks());
        } else if (action instanceof ReplaceAction) {
            ReplaceAction replaceAction = (ReplaceAction) action;
            replaceAction.invoke(entityInstance, grid, ticksCounter.getTicks());
        } else if (action instanceof ProximityAction) {
            ProximityAction proximityAction = (ProximityAction) action;
            proximityAction.invoke(entityInstance, grid, ticksCounter.getTicks());
        }
    }

    /**
     * Getting the secondary entity instances list and invoke the action on each secondary entity instance and the primary entity instance.
     */
    private void invokeActionsWithSecondaryEntity(EntityInstance entityInstance, Action actionToInvoke) throws CrashException {
        List<EntityInstance> secondaryInstances = getSecondaryInstances(actionToInvoke.getSecondaryEntity());

        for (EntityInstance secondaryEntityInstance : secondaryInstances) {
            if (actionToInvoke instanceof OneEntAction) {
                OneEntAction oneEntAction = (OneEntAction) actionToInvoke;
                oneEntAction.invokeWithSecondary(entityInstance, secondaryEntityInstance, ticksCounter.getTicks());
            } else if (actionToInvoke instanceof CalculationAction) {
                CalculationAction calculationAction = (CalculationAction) actionToInvoke;
                calculationAction.invokeWithSecondary(entityInstance, secondaryEntityInstance, ticksCounter.getTicks());
            } else if (actionToInvoke instanceof AbstractConditionAction) {
                AbstractConditionAction abstractConditionAction = (AbstractConditionAction) actionToInvoke;
                abstractConditionAction.invokeWithSecondary(entityInstance, secondaryEntityInstance, grid, ticksCounter.getTicks());
            } else if (actionToInvoke instanceof ProximityAction) {
                ProximityAction proximityAction = (ProximityAction) actionToInvoke;
                proximityAction.invokeWithSecondary(entityInstance, secondaryEntityInstance, grid, ticksCounter.getTicks());
            } else if (actionToInvoke instanceof ReplaceAction) {
                ReplaceAction replaceAction = (ReplaceAction) actionToInvoke;
                replaceAction.invokeWithSecondary(entityInstance, secondaryEntityInstance, grid, ticksCounter.getTicks());
            }
        }
    }

    /**
     * Calculate the secondary entity instances create a list with the instances and return this list.
     */
    private List<EntityInstance> getSecondaryInstances(AbstractAction.SecondaryEntity secondaryEntity) throws CrashException {
        List<EntityInstance> entityInstances = new ArrayList<>();
        AbstractConditionAction conditionAction = (AbstractConditionAction) secondaryEntity.getCondition();
        Entity secondaryEntityRef = entities.get(secondaryEntity.getContextEntity());
        int count = secondaryEntity.getCount();

        if (count == constAll) {
            entityInstances.addAll(secondaryEntityRef.getEntityInstances());
        } else {
            if (count > secondaryEntityRef.getCurrentPopulation()) {
                count = secondaryEntityRef.getCurrentPopulation();
            }

            int i = 0;
            while (i < count) {
                if (conditionAction != null) {
                    EntityInstance entityInstanceToAdd = secondaryEntityRef.getRandomEntityInstance();
                    if (conditionAction.getConditionResult(entityInstanceToAdd)) {
                        entityInstances.add(entityInstanceToAdd);
                        i++;
                    }
                } else {
                    entityInstances.add(secondaryEntityRef.getRandomEntityInstance());
                    i++;
                }
            }
        }

        return entityInstances;
    }


    public void resetWorld() {
        ticksCounter.resetTicks();
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
        return isEndingBySecondsMet() || isEndingByTicksMet() || userInstructions.isSimulationStopped || status == SimulationStatus.CRUSHED;
    }

    /**
     * @return If the simulation has met the required amount of ticks to stop it.
     */
    private boolean isEndingByTicksMet() {
        boolean ret = false;

        if (endingConditions.containsKey(EndingConditionType.TICKS)) {
            if (ticksCounter.getTicks() >= endingConditions.get(EndingConditionType.TICKS).getCount()) {
                terminateCondition = endingConditions.get(EndingConditionType.TICKS);
                ret = true;
            }
        }

        return ret;
    }

    /**
     * @return If the simulation has met the required amount of seconds to stop it.
     */
    private boolean isEndingBySecondsMet() {
        boolean ret = false;

        if (endingConditions.containsKey(EndingConditionType.SECONDS)) {
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
        SimulationInstance toCompare = (SimulationInstance) obj;
        return (toCompare.endingConditions.equals(this.endingConditions)) && (toCompare.rules.equals(this.rules)) && (toCompare.entities.equals(entities)) && (toCompare.environmentProperties.equals(this.environmentProperties));
    }

    /**
     * Tries to set the entity's starting population according to the user's input.
     * Will do so only if the user is trying to set the population according to
     *
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
                return new SetResponse(true, String.format("You have successfully changed %s's starting population to %s.", entities.get(input.getName()).getName(), input.getPopulation()), null);
            }
        } else {
            if (totalPopulation + input.getPopulation() > gridSize) {
                return populationErrorMessage(gridSize);
            } else {
                totalPopulation += input.getPopulation();
                return new SetResponse(true, String.format("You have successfully set %s's starting population to %s.", entities.get(input.getName()).getName(), input.getPopulation()), null);
            }
        }
    }

    private SetResponse populationErrorMessage(int gridSize) {
        return new SetResponse(false, String.format("ERROR: You are trying to set more entities than the grid size allows.\n" +
                "You can only add %s entity instances at most.\n" +
                "The maximum allowed number of entities is %s.", gridSize - totalPopulation, gridSize), null);
    }

    /**
     * In func we add the simulation to the thread-pool. If the simulation has no population
     * at all it will send back a response indicating the simulation was not added.
     */
    public boolean isStartable() {
        int populationCount = 0;
        for (Entity e : entities.values()
        ) {
            populationCount += e.getStartingPopulation();
        }

        return populationCount > 0;
    }

    private void initGrid() {
        List<EntityInstance> allEntityInstances = new ArrayList<>();
        for (Entity entity : entities.values()) {
            allEntityInstances.addAll(entity.getEntityInstances());
        }

        grid.populateGrid(allEntityInstances);
    }

    private void initInstances() {
        for (Entity entity : entities.values()) {
            entity.resetPopulation();
        }
    }


    private void fetchTicksExpressions() {
        for (Rule rule : rules.values()) {
            for (Action action : rule.getActions()) {
                if (action instanceof AbstractConditionAction) {
                    AbstractConditionAction abstractConditionAction = (AbstractConditionAction) action;
                    if (abstractConditionAction instanceof MultipleCondition) {
                        checkForTicksInSubMultipleConditions(((MultipleCondition) abstractConditionAction).getSubConditions());
                    }
                    if (abstractConditionAction.getThenActions() != null) {
                        checkForTicksInSubActions(abstractConditionAction.getThenActions().getActionsToInvoke());
                    }

                    if (abstractConditionAction.getElseActions() != null) {
                        checkForTicksInSubActions(abstractConditionAction.getElseActions().getActionsToInvoke());
                    }

                } else if (action instanceof ProximityAction) {
                    ProximityAction proximityAction = (ProximityAction) action;
                    if (proximityAction.getProximityActions() != null) {
                        checkForTicksInSubActions(proximityAction.getProximityActions().getActionsToInvoke());
                    }
                }

                if (action.getContextProperty() != null && action.getContextProperty() instanceof TicksExpression) {
                    TicksExpression ticksExpression = (TicksExpression) action.getContextProperty();
                    ticksExpression.setSimulationTicks(ticksCounter);
                }

                if (action.getValueExpression() != null && action.getValueExpression() instanceof TicksExpression) {
                    TicksExpression ticksExpression = (TicksExpression) action.getValueExpression();
                    ticksExpression.setSimulationTicks(ticksCounter);
                }
            }
        }
    }

    private void checkForTicksInSubActions(List<Action> subActions) {
        for (Action action : subActions) {
            if (action instanceof AbstractConditionAction) {
                AbstractConditionAction abstractConditionAction = (AbstractConditionAction) action;
                if (abstractConditionAction.getThenActions() != null) {
                    checkForTicksInSubActions(abstractConditionAction.getThenActions().getActionsToInvoke());
                }

                if (abstractConditionAction.getElseActions() != null) {
                    checkForTicksInSubActions(abstractConditionAction.getElseActions().getActionsToInvoke());
                }

            } else if (action instanceof ProximityAction) {
                ProximityAction proximityAction = (ProximityAction) action;
                if (proximityAction.getProximityActions() != null) {
                    checkForTicksInSubActions(proximityAction.getProximityActions().getActionsToInvoke());
                }
            }

            if (action.getContextProperty() != null && action.getContextProperty() instanceof TicksExpression) {
                TicksExpression ticksExpression = (TicksExpression) action.getContextProperty();
                ticksExpression.setSimulationTicks(ticksCounter);
            }

            if (action.getValueExpression() != null && action.getValueExpression() instanceof TicksExpression) {
                TicksExpression ticksExpression = (TicksExpression) action.getValueExpression();
                ticksExpression.setSimulationTicks(ticksCounter);
            }
        }
    }

    private void checkForTicksInSubMultipleConditions(List<AbstractConditionAction> subConditions) {
        for (AbstractConditionAction action : subConditions) {
            if (action.getContextProperty() != null && action.getContextProperty() instanceof TicksExpression) {
                TicksExpression ticksExpression = (TicksExpression) action.getContextProperty();
                ticksExpression.setSimulationTicks(ticksCounter);
            }

            if (action.getValueExpression() != null && action.getValueExpression() instanceof TicksExpression) {
                TicksExpression ticksExpression = (TicksExpression) action.getValueExpression();
                ticksExpression.setSimulationTicks(ticksCounter);
            }
        }
    }

    private void fetchReplaceActions() {
        Entity entityForReplace;

        for (Rule rule : rules.values()) {
            for (Action action : rule.getActions()) {
                if (action instanceof ReplaceAction) {
                    ReplaceAction replaceAction = (ReplaceAction) action;
                    entityForReplace = entities.get(replaceAction.getNewEntityName());
                    replaceAction.setNewEntity(entityForReplace);
                } else if (action instanceof AbstractConditionAction) {
                    AbstractConditionAction abstractConditionAction = (AbstractConditionAction) action;
                    if (abstractConditionAction.getThenActions() != null) {
                        checkForReplaceInSubActions(abstractConditionAction.getThenActions().getActionsToInvoke());
                    }

                    if (abstractConditionAction.getElseActions() != null) {
                        checkForReplaceInSubActions(abstractConditionAction.getElseActions().getActionsToInvoke());
                    }
                } else if (action instanceof ProximityAction) {
                    ProximityAction proximityAction = (ProximityAction) action;
                    if (proximityAction.getProximityActions() != null) {
                        checkForReplaceInSubActions(proximityAction.getProximityActions().getActionsToInvoke());
                    }
                }
            }
        }
    }

    private void checkForReplaceInSubActions(List<Action> subActions) {
        Entity entityForReplace;

        for (Action action : subActions) {
            if (action instanceof ReplaceAction) {
                ReplaceAction replaceAction = (ReplaceAction) action;
                entityForReplace = entities.get(replaceAction.getNewEntityName());
                replaceAction.setNewEntity(entityForReplace);
            } else if (action instanceof AbstractConditionAction) {
                AbstractConditionAction abstractConditionAction = (AbstractConditionAction) action;
                if (abstractConditionAction.getThenActions() != null) {
                    checkForReplaceInSubActions(abstractConditionAction.getThenActions().getActionsToInvoke());
                }

                if (abstractConditionAction.getElseActions() != null) {
                    checkForReplaceInSubActions(abstractConditionAction.getElseActions().getActionsToInvoke());
                }
            } else if (action instanceof ProximityAction) {
                ProximityAction proximityAction = (ProximityAction) action;
                if (proximityAction.getProximityActions() != null) {
                    checkForReplaceInSubActions(proximityAction.getProximityActions().getActionsToInvoke());
                }
            }
        }
    }


    private void initSimulation() {
        this.startingTime = System.currentTimeMillis();
        this.status = SimulationStatus.ONGOING;
        userInstructions.isSimulationRunning = true;
        initInstances();
        initGrid();
        fetchTicksExpressions();
        fetchReplaceActions();
    }

    private void checkPopulation() throws CrashException {
        int populationSum = 0;

        for (Entity entity : entities.values()) {
            populationSum += entity.getCurrentPopulation();
        }

        if (populationSum > grid.getRows() * grid.getColumns()) {
            throw new CrashException("Simulation crushed: population count is greater than the grid size!");
        }
    }

    private void threadSleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
