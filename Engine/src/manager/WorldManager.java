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
    public void loadSimulationFromFile(String path) {
        if (Reader.isValidPath(path)) {
            this.world = Reader.readWorldFromXML(path);
        }
    }

    @Override
    public void runSimulation() {

    }
}
