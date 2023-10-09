package manager;

import server2client.simulation.execution.SetResponse;
import server2client.simulation.execution.StartResponse;
import server2client.simulation.load.result.DTOLoadResult;
import server2client.simulation.prview.PreviewData;
import server2client.simulation.runtime.ResultData;
import server2client.simulation.runtime.SimulationRunData;
import server2client.simulation.runtime.generator.IdGenerator;
import server2client.simulation.genral.impl.properties.DTOEnvironmentVariable;
import server2client.simulation.genral.impl.properties.StartData;
import javafx.application.Platform;
import jaxb.event.FileLoadedEvent;
import jaxb.unmarshal.Reader;
import manager.DTO.creator.DTOCreator;
import manager.execution.ExecutionManager;
import manager.validator.exceptions.IllegalBooleanValueException;
import manager.validator.exceptions.IllegalStringValueException;
import manager.validator.exceptions.OutOfRangeException;
import manager.validator.validator.InputValidator;
import simulation.objects.world.SimulationInstance;
import simulation.objects.world.definition.SimulationDefinition;
import simulation.objects.world.status.SimulationStatus;
import client2server.simulation.control.bar.DTOSimulationControlBar;
import client2server.simulation.execution.user.input.EntityPopulationUserInput;
import client2server.simulation.execution.user.input.EnvPropertyUserInput;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.BoolRndValueGen;
import simulation.properties.property.random.value.impl.DoubleRndValueGen;
import simulation.properties.property.random.value.impl.IntRndValueGen;
import simulation.properties.property.random.value.impl.StringRndValueGen;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import java.util.*;

public class SimulationManager implements EngineInterface {
    private SimulationInstance simulationDefinition;
    private Map<String, SimulationDefinition> simulationDefinitions;
    private Map<String, ResultData> pastSimulations;
    private Set<String> keysToSerialize;
    private boolean isSimulationLoaded;
    private boolean isFirstSimulationLoaded;
    private ExecutionManager executionManager = null;

    public SimulationManager() {
        simulationDefinition = null;
        simulationDefinitions = new HashMap<>();
        pastSimulations = new HashMap<>();
        isSimulationLoaded = false;
        keysToSerialize = new HashSet<>();
        isFirstSimulationLoaded = true;
    }

    public void loadValuesFromDeserialization(SimulationManager instance) {
        simulationDefinition = instance.simulationDefinition;
        pastSimulations = instance.pastSimulations;
        keysToSerialize = instance.keysToSerialize;
        isSimulationLoaded = instance.isSimulationLoaded;
    }

    @Override
    public boolean getIsSimulationLoaded() {
        return isSimulationLoaded;
    }

    private PreviewData getCurrentSimulationDetails() {
        DTOCreator dtoCreator = new DTOCreator();

        return dtoCreator.createSimulationPreviewDataObject(simulationDefinition.getEnvironmentProperties(), simulationDefinition.getEntities(), simulationDefinition.getRules(), simulationDefinition.getEndingConditions(), simulationDefinition.getGrid(), simulationDefinition.getThreadCount());
    }

    @Override
    public String getSimulationDetailsById(int simId) {
        return null;
    }

    @Override
    public ResultData[] getPastSimulationResultData() {
        return pastSimulations.values().toArray(new ResultData[0]);
    }

//    private void addResultData(ResultData resultData) {
//        pastSimulations.put(resultData.getId(), resultData);
//    }

    private ResultData getResultDataById(String id) {
        return pastSimulations.get(id);
    }

    @Override
    public DTOLoadResult loadSimulationFromFile(File file) {
        SimulationDefinition newDefinition = null;
        DTOLoadResult dtoLoadResult = null;
        try {
            newDefinition = Reader.readWorldFromXML(file, simulationDefinitions);
            if (newDefinition.getSimulationAbstractInstance() != null) {
                simulationDefinitions.put(newDefinition.getSimulationName(), newDefinition);
                dtoLoadResult = new DTOLoadResult(true, "");

                isFirstSimulationLoaded = false;
                if (executionManager != null){
                    executionManager.shutdownThreadPool();
                }

                //executionManager = new ExecutionManager(simulationDefinition.getThreadCount());
            }

            isSimulationLoaded = true;
        }catch (IllegalArgumentException e){
            dtoLoadResult = new DTOLoadResult(false, e.getMessage());
        }


        return dtoLoadResult;
    }

    @Override
    public StartResponse startSimulation() {
        // TODO: adjust to the new construction -> this function needs to receive a simulation name first.
        if(simulationDefinition.isStartable()) {
            DTOCreator dtoCreator = new DTOCreator();
            String id = IdGenerator.generateID();
            SimulationRunData simulationRunData = new SimulationRunData(IdGenerator.generateID(),0, 0, dtoCreator.getDTOEntityPopulationList(simulationDefinition.getEntities()), SimulationStatus.WAITING.name(), false, getEnvVarsValuesMap(), false);

            addSimulationToQueue(simulationRunData);
            return new StartResponse(true, String.format("Simulation %s was added to the queue successfully.", id), simulationRunData);
        } else {
            return new StartResponse(false, "ERROR: Could not start simulation. You need to have at least one entity with a population larger than 0.");
        }

    }

    private Map<String, Object> getEnvVarsValuesMap() {
        // TODO: adjust to the new construction -> this function needs to receive a simulation name first.
        Map<String, Object> envVarsValuesMap = new HashMap<>();
        Map<String, Property> environmentProperties = simulationDefinition.getEnvironmentProperties();

        for (Property property : environmentProperties.values()) {
            envVarsValuesMap.put(property.getName(), property.getValue());
        }

        return envVarsValuesMap;
    }

    /**
     * Invokes all listeners for a successful load of a file.
     */
    private void invokeSuccessLoadListeners(List<EventListener> listeners) {
        PreviewData previewData = getCurrentSimulationDetails();
        Platform.runLater(() -> {
            for (EventListener f : listeners) {
                FileLoadedEvent fileLoadedEvent = (FileLoadedEvent) f;
                fileLoadedEvent.onFileLoaded(previewData, isFirstSimulationLoaded);
            }
        });
    }

    /**
     * Clears all data of past simulations and all generated IDs.
     */
    public void resetEngine() {
        pastSimulations.clear();
        IdGenerator.clearIds();
    }

    /**
     * This method generate a random object match to the environment variable's type.
     *
     * @param envProperty the environment variable
     * @return a random object
     */
    private Object getRandomValueByType(Property envProperty) {
        Object ret = null;


        switch (envProperty.getType()) {
            case DECIMAL:
                IntProperty intProperty = (IntProperty) envProperty;
                RandomValueGenerator<Integer> randomIntValueGenerator = new IntRndValueGen(intProperty.getFrom(), intProperty.getTo());
                ret = randomIntValueGenerator.generateRandomValue();
                break;
            case FLOAT:
                DoubleProperty doubleProperty = (DoubleProperty) envProperty;
                RandomValueGenerator<Double> randomDoubleValueGenerator = new DoubleRndValueGen(doubleProperty.getFrom(), doubleProperty.getTo());
                ret = randomDoubleValueGenerator.generateRandomValue();
                break;
            case BOOLEAN:
                RandomValueGenerator<Boolean> randomBooleanValueGenerator = new BoolRndValueGen();
                ret = randomBooleanValueGenerator.generateRandomValue();
                break;
            case STRING:
                RandomValueGenerator<String> randomStringValueGenerator = new StringRndValueGen();
                ret = randomStringValueGenerator.generateRandomValue();
                break;
        }

        return ret;
    }


    /**
     * Create and return the DTO start data which contains information about the simulation's environment variables.
     *
     * @return a StartData DTO
     */
    @Override
    public StartData getSimulationStartData() {
        // TODO: adjust to the new construction -> this function needs to receive a simulation name first.
        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>();
        Map<String, Property> environmentProperties = this.simulationDefinition.getEnvironmentProperties();
        Property valueFromTheMap;

        for (Map.Entry<String, Property> entry : environmentProperties.entrySet()) {
            valueFromTheMap = entry.getValue();
            environmentVariables.add(getDTOEnvironmentVariable(valueFromTheMap));
        }

        return new StartData(environmentVariables);
    }

    /**
     * Create a 'DTOEnvironmentVariable' which contain the given environment variable's data and return it.
     */
    private DTOEnvironmentVariable getDTOEnvironmentVariable(Property valueFromTheMap) {
        String name = valueFromTheMap.getName(), type = valueFromTheMap.getType().toString().toLowerCase();
        Double from = null, to = null;

        if (valueFromTheMap.getType() == PropertyType.FLOAT) {
            DoubleProperty doubleProperty = (DoubleProperty) valueFromTheMap;
            from = doubleProperty.getFrom();
            to = doubleProperty.getTo();
        } else if (valueFromTheMap.getType() == PropertyType.DECIMAL) {
            IntProperty intProperty = (IntProperty) valueFromTheMap;
            from = (double) intProperty.getFrom();
            to = (double) intProperty.getTo();
        }

        return new DTOEnvironmentVariable(name, type, from, to);
    }

    public void saveState(String path) {
        File toSerialize = new File(path);
        keysToSerialize.addAll(IdGenerator.getGeneratedIds());

        try {
            toSerialize.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(this);
            out.close();
            fileOut.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadState(String path) {
        try {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);
            loadValuesFromDeserialization((SimulationManager) in.readObject());
            IdGenerator.setGeneratedIds(keysToSerialize);
            in.close();
            file.close();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public SetResponse setEntityPopulation(EntityPopulationUserInput input) {
        // TODO: adjust to the new construction -> this function needs to receive a simulation name first.
        return simulationDefinition.setEntityPopulation(input);
    }

    @Override
    public SetResponse setEnvironmentVariable(EnvPropertyUserInput input) {
        // TODO: adjust to the new construction -> this function needs to receive a simulation name first.
        SetResponse response;
        Map<String, Property> environmentProperties = this.simulationDefinition.getEnvironmentProperties();
        Property envProperty = environmentProperties.get(input.getName());

        if (input.isRandomInit()) {
            envProperty.updateValueAndIsRandomInit(getRandomValueByType(envProperty), input.isRandomInit());
            response = new SetResponse(true, String.format("%s's value was set to %s", input.getName(), input.getValue()), null);
        } else {
            response = validateEnvVarInitValue(envProperty, input.getValue());
            if (response.isSuccess()) {
                envProperty.updateValueAndIsRandomInit(response.getParsedValue(), input.isRandomInit());
            }
        }

        return response;
    }


    @Override
    public SimulationRunData getRunDataById(String simId) {
        return executionManager.getRunDataById(simId);
    }

    /**
     * Validates the given user input for the environment variable and returns a set response accordingly.
     * Validates range\regex matching\value compatibility based on the type of the property.
     *
     * @param property   The property we are comparing the value to
     * @param inputValue The value we want to compare
     * @return A Set response indicating if the set action has succeeded or failed with a custom message
     */
    private SetResponse validateEnvVarInitValue(Property property, Object inputValue) {
        String successMessage = String.format("%s's value was successfully set to %s", property.getName(), inputValue.toString());
        InputValidator inputValidator = new InputValidator();
        try {
            switch (property.getType()) {
                case DECIMAL: {

                    int value = Integer.parseInt(inputValue.toString());
                    IntProperty intProperty = (IntProperty) property;
                    inputValidator.isIntegerInRange(value, intProperty.getFrom(), intProperty.getTo());
                    return new SetResponse(true, successMessage, value);
                }
                case FLOAT: {
                    double value = Double.parseDouble(inputValue.toString());
                    DoubleProperty doubleProperty = (DoubleProperty) property;
                    inputValidator.isDoubleInRange(value, doubleProperty.getFrom(), doubleProperty.getTo());
                    return new SetResponse(true, successMessage, value);
                }
                case BOOLEAN: {
                    inputValidator.validateBoolean(inputValue.toString());
                    if((inputValue.toString().equalsIgnoreCase("true"))) {
                        return new SetResponse(true, successMessage, true);
                    } else {
                        return new SetResponse(true, successMessage, false);
                    }
                }
                case STRING:
                    inputValidator.validateStringValue(inputValue.toString());
                    return new SetResponse(true, successMessage, inputValue);
            }
        } catch (NumberFormatException e) {
            return new SetResponse(false, String.format("ERROR: Value provided is does not match %s's type (%s)", property.getName(), property.getType().toString()), null);
        } catch (OutOfRangeException | IllegalBooleanValueException e) {
            return new SetResponse(false, String.format("ERROR: Value given is not in %s's range.", property.getName()), null);
        } catch (IllegalStringValueException e) {
            return new SetResponse(false, "ERROR: String given does not meet the string guidelines.", null);
        }
        return null;
    }

    private void addSimulationToQueue(SimulationRunData simulationRunData) {
        // TODO: adjust to the new construction -> this function needs to receive a simulation name first.
        SimulationInstance simulationInstance = new SimulationInstance(simulationDefinition);
        simulationInstance.setSimulationId(simulationRunData.getSimId());
        // TODO: send null to the executionManager temporarily until we fetch previous methods.
        executionManager.addSimulationToQueue(null,simulationInstance, simulationRunData);
    }

    @Override
    public void shutdownThreadPool() {
        if(executionManager != null){
            executionManager.shutdownThreadPool();
        }
    }

    @Override
    public void setStopPausePlayOrSkipFwdForSimById(String simId, DTOSimulationControlBar dtoSimulationControlBar) {
        executionManager.setStopPausePlayOrSkipFwdForSimById(simId, dtoSimulationControlBar);
    }
}
