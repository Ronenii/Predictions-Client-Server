package manager;

import engine2ui.simulation.result.ResultData;
import engine2ui.simulation.start.DTOEnvironmentVariable;
import engine2ui.simulation.start.StartData;
import jaxb.unmarshal.Reader;
import simulation.objects.world.World;
import ui2engine.simulation.func1.DTOFirstFunction;
import simulation.properties.property.api.Property;
import simulation.properties.property.api.PropertyType;
import simulation.properties.property.impl.BooleanProperty;
import simulation.properties.property.impl.DoubleProperty;
import simulation.properties.property.impl.IntProperty;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.BoolRndValueGen;
import simulation.properties.property.random.value.impl.DoubleRndValueGen;
import simulation.properties.property.random.value.impl.IntRndValueGen;
import simulation.properties.property.random.value.impl.StringRndValueGen;
import ui2engine.simulation.func3.DTOThirdFunction;
import ui2engine.simulation.func3.user.input.EnvPropertyUserInput;

import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.util.*;

public class WorldManager implements EngineInterface {
    private World world;
    private Map<String, ResultData> pastSimulations;

    public WorldManager() {
        world = null;
        pastSimulations = new HashMap<>();

        //TODO: DEBUG
        ResultData r1 = new ResultData("23-01-2010 | 07:33:03");

        ResultData r2 = new ResultData("07-11-1997 | 00:31:59");

        ResultData r3 = new ResultData("09-10-1999 | 12:32:15");

        addResultData(r1);
        addResultData(r2);
        addResultData(r3);
    }

    @Override
    public String getCurrentSimulationDetails() {
        return null;
    }

    @Override
    public String getSimulationDetailsById(int simId) {
        return null;
    }

    @Override
    public ResultData[] getPastSimulationResultData() {
        return pastSimulations.values().toArray(new ResultData[0]);
    }

    private void addResultData(ResultData resultData){
        pastSimulations.put(resultData.getId(), resultData);
    }

    private ResultData getResultDataById(String id){
        return pastSimulations.get(id);
    }

    @Override
    public void loadSimulationFromFile(DTOFirstFunction dto) {
        if (Reader.isValidPath(dto.getPath())) {
            this.world = Reader.readWorldFromXML(dto.getPath());
        }
    }

    @Override
    public void runSimulation(DTOThirdFunction dtoThirdFunction) {
        // fetch the user data input into the simulation's environment properties.
        fetchDTOThirdFunctionObject(dtoThirdFunction);
        // run the simulation.
        this.world.invoke();
        // TODO : add the simulation result data to 'pastSimulations' and return to the UI these results.
    }


    /**
     * Get the third function's DTO object, extract the user input from this object and update the simulation's environment variables.
     *
     * @param dtoThirdFunction the third function's DTO object
     */
    private void fetchDTOThirdFunctionObject(DTOThirdFunction dtoThirdFunction){
        Set<EnvPropertyUserInput> envPropertyUserInputs = dtoThirdFunction.getEnvPropertyUserInputs();
        Map<String, Property> environmentProperties = this.world.getEnvironmentProperties();
        Property envProperty;

        for (EnvPropertyUserInput envPropertyUserInput : envPropertyUserInputs){
            envProperty = environmentProperties.get(envPropertyUserInput.getName());
            if(envPropertyUserInput.isRandomInit()){
                envProperty.updateValueAndIsRandomInit(getRandomValueByType(envProperty),envPropertyUserInput.isRandomInit());
            }
            else {
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
    private Object getRandomValueByType(Property envProperty){
        Object ret = null;

        switch (envProperty.getType()){
            case INT:
                IntProperty intProperty = (IntProperty)envProperty;
                RandomValueGenerator<Integer> randomIntValueGenerator = new IntRndValueGen(intProperty.getFrom(), intProperty.getTo());
                ret = randomIntValueGenerator.generateRandomValue();
                break;
            case DOUBLE:
                DoubleProperty doubleProperty = (DoubleProperty)envProperty;
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
    private DTOEnvironmentVariable getDTOEnvironmentVariable(Property valueFromTheMap){
        String name = valueFromTheMap.getName(), type = valueFromTheMap.getType().toString().toLowerCase();
        Double from = null, to = null;

        if(valueFromTheMap.getType() == PropertyType.DOUBLE){
            DoubleProperty doubleProperty = (DoubleProperty)valueFromTheMap;
            from = doubleProperty.getFrom();
            to = doubleProperty.getTo();
        } else if (valueFromTheMap.getType() == PropertyType.INT) {
            IntProperty intProperty = (IntProperty)valueFromTheMap;
            from = (double)intProperty.getFrom();
            to = (double)intProperty.getTo();
        }

        return new DTOEnvironmentVariable(name,type,from,to);
    }
}
