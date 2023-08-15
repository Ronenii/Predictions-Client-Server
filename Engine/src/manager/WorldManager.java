package manager;

import engine2ui.simulation.result.ResultData;
import engine2ui.simulation.start.StartData;
import jaxb.unmarshal.Reader;
import simulation.objects.world.World;
import simulation.properties.property.api.Property;
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

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WorldManager implements EngineInterface {
    private World world;
    private Map<UUID, ResultData> pastSimulations;

    public WorldManager() {
        world = null;
        pastSimulations = new HashMap<>();
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
    public String[] getAllSimulationDetailsInShortFormat() {
        return new String[0];
    }

    @Override
    public void loadSimulationFromFile(String path) {
        if (Reader.isValidPath(path)) {
            this.world = Reader.readWorldFromXML(path);
        }
    }

    @Override
    public void runSimulation(DTOThirdFunction dtoThirdFunction) {

        fetchDTOThirdFunctionObject(dtoThirdFunction);

    }

    @Override
    public StartData getSimulationStartData() {
        return null;
    }

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
}
