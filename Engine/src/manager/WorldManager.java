package manager;

import engine2ui.simulation.execution.SetResponse;
import engine2ui.simulation.genral.impl.properties.DTOEndingCondition;
import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.prview.PreviewData;
import engine2ui.simulation.result.ResultData;
import engine2ui.simulation.result.ResultInfo;
import engine2ui.simulation.result.generator.IdGenerator;
import engine2ui.simulation.genral.impl.properties.DTOEnvironmentVariable;
import engine2ui.simulation.genral.impl.properties.StartData;
import jaxb.event.FileLoadedEvent;
import jaxb.unmarshal.Reader;
import manager.DTO.creator.DTOCreator;
import manager.validator.exceptions.IllegalBooleanValueException;
import manager.validator.exceptions.IllegalStringValueException;
import manager.validator.exceptions.OutOfRangeException;
import manager.validator.validator.InputValidator;
import simulation.objects.world.World;
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;
import ui2engine.simulation.execution.user.input.EnvPropertyUserInput;
import ui2engine.simulation.load.DTOLoadFile;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.BoolRndValueGen;
import simulation.properties.property.random.value.impl.DoubleRndValueGen;
import simulation.properties.property.random.value.impl.IntRndValueGen;
import simulation.properties.property.random.value.impl.StringRndValueGen;
import ui2engine.simulation.execution.DTOExecutionData;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import java.util.*;

public class WorldManager implements EngineInterface, Serializable {
    private World world;
    private Map<String, ResultData> pastSimulations;
    private Set<String> keysToSerialize;
    private boolean isSimulationLoaded;

    public WorldManager() {
        world = null;
        pastSimulations = new HashMap<>();
        isSimulationLoaded = false;
        keysToSerialize = new HashSet<>();
    }

    public void loadValuesFromDeserialization(WorldManager instance) {
        world = instance.world;
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

        return dtoCreator.createSimulationPreviewDataObject(world.getEnvironmentProperties(), world.getEntities(), world.getRules(), world.getEndingConditions(), world.getGrid(), world.getThreadCount());
    }

    @Override
    public String getSimulationDetailsById(int simId) {
        return null;
    }

    @Override
    public ResultData[] getPastSimulationResultData() {
        return pastSimulations.values().toArray(new ResultData[0]);
    }

    private void addResultData(ResultData resultData) {
        pastSimulations.put(resultData.getId(), resultData);
    }

    private ResultData getResultDataById(String id) {
        return pastSimulations.get(id);
    }

    @Override
    public DTOLoadSucceed loadSimulationFromFile(DTOLoadFile dto) {
        DTOLoadSucceed dtoLoadSucceed = new DTOLoadSucceed(false);
        Reader.validatePath(dto.getFile().getPath());

        this.world = Reader.readWorldFromXML(dto.getFile());
        if (this.world != null) {
            dtoLoadSucceed = new DTOLoadSucceed(true);
            invokeSuccessLoadListeners(dto.getListeners());
        }

        isSimulationLoaded = true;
        return dtoLoadSucceed;
    }

    private void invokeSuccessLoadListeners(List<EventListener> listeners) {
        PreviewData previewData = getCurrentSimulationDetails();

        for (EventListener f : listeners) {
            FileLoadedEvent fileLoadedEvent = (FileLoadedEvent) f;
            fileLoadedEvent.onFileLoaded(previewData);
        }
    }

    @Override
    public ResultInfo runSimulation(DTOExecutionData dtoExecutionData) {
        // Resets all entities in this world
        world.resetWorld();

        // fetch the user data input into the simulation's environment properties.
        fetchDTOThirdFunctionObject(dtoExecutionData);

        // TODO: implement function to fetch '

        // run the simulation.
        ResultData result = this.world.runSimulation();
        this.pastSimulations.put(result.getId(), result);

        // Sent to the UI the termination cause.
        DTOEndingCondition dtoEndingCondition = new DTOEndingCondition(world.getTerminateCondition().getType().toString(), world.getTerminateCondition().getCount());
        return new ResultInfo(result.getId(), dtoEndingCondition);
    }

    /**
     * Clears all data of past simulations and all generated IDs.
     */
    public void resetEngine() {
        pastSimulations.clear();
        ResultData.clearIds();
    }

    /**
     * Get the third function's DTO object, extract the user input from this object and update the simulation's environment variables.
     *
     * @param dtoExecutionData the third function's DTO object
     */
    private void fetchDTOThirdFunctionObject(DTOExecutionData dtoExecutionData) {
        Map<String, EnvPropertyUserInput> envPropertyUserInputs = dtoExecutionData.getEnvPropertyUserInputs();
        Map<String, Property> environmentProperties = this.world.getEnvironmentProperties();
        Property envProperty;

        for (EnvPropertyUserInput envPropertyUserInput : envPropertyUserInputs.values()) {
            envProperty = environmentProperties.get(envPropertyUserInput.getName());
            if (envPropertyUserInput.isRandomInit()) {
                envProperty.updateValueAndIsRandomInit(getRandomValueByType(envProperty), envPropertyUserInput.isRandomInit());
            } else {
                envProperty.updateValueAndIsRandomInit(envPropertyUserInput.getValue(), envPropertyUserInput.isRandomInit());
            }
        }
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
        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>();
        Map<String, Property> environmentProperties = this.world.getEnvironmentProperties();
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
            loadValuesFromDeserialization((WorldManager) in.readObject());
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
        return world.setEntityInput(input);
    }

    @Override
    public SetResponse setEnvironmentVariable(EnvPropertyUserInput input) {

        SetResponse response;
        Map<String, Property> environmentProperties = this.world.getEnvironmentProperties();
        Property envProperty = environmentProperties.get(input.getName());

        if (input.isRandomInit()) {
            envProperty.updateValueAndIsRandomInit(getRandomValueByType(envProperty), input.isRandomInit());
            response = new SetResponse(true, String.format("%s's value was set to %s", input.getName(), input.getValue()));
        } else {
            response = validateEnvVarInitValue(envProperty, input.getValue());
            if (response.isSuccess()) {
                envProperty.updateValueAndIsRandomInit(input.getValue(), input.isRandomInit());
            }
        }

        return response;
    }

    /**
     * Validates the given user input for the environment variable and returns a set response accordingly.
     * Validates range\regex matching\value compatibility based on the type of the property.
     * @param property The property we are comparing the value to
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

                    return new SetResponse(true, successMessage);
                }
                case FLOAT: {
                    double value = Double.parseDouble(inputValue.toString());
                    DoubleProperty doubleProperty = (DoubleProperty) property;

                    inputValidator.isDoubleInRange(value, doubleProperty.getFrom(), doubleProperty.getTo());

                    return new SetResponse(true, successMessage);
                }
                case BOOLEAN: {
                    inputValidator.validateBoolean(inputValue.toString());
                    return new SetResponse(true, successMessage);
                }
                case STRING:
                    inputValidator.validateStringValue(inputValue.toString());
                    return new SetResponse(true, successMessage);
            }
        } catch (NumberFormatException e) {
            return new SetResponse(false, String.format("ERROR: Value provided is does not match %s's type (%s)", property.getName(), property.getType().toString()));
        } catch(OutOfRangeException | IllegalBooleanValueException e) {
            return new SetResponse(false, String.format("ERROR: Value given is not in %s's range.",property.getName()));
        } catch (IllegalStringValueException e) {
            return new SetResponse(false, "ERROR: String given does not meet the string guidelines.");
        }
        return null;
    }
}
