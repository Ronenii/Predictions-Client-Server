package manager;

import engine2ui.simulation.result.ResultData;
import jaxb.unmarshal.Reader;
import simulation.objects.world.World;

import java.util.HashMap;
import java.util.Map;

public class WorldManager implements EngineInterface {
    private World world;
    private final Map<String, ResultData> pastSimulations;

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
    public void loadSimulationFromFile(String path) {
        if (Reader.isValidPath(path)) {
            this.world = Reader.readWorldFromXML(path);
        }
    }

    @Override
    public void runSimulation() {

    }
}
