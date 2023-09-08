package manager;

import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.result.ResultInfo;
import engine2ui.simulation.genral.impl.properties.StartData;
import ui2engine.simulation.func3.DTOExecutionData;

import engine2ui.simulation.result.ResultData;
import ui2engine.simulation.load.DTOLoadFile;

public interface EngineInterface {

    String getSimulationDetailsById(int simId);

    ResultData[] getPastSimulationResultData();

    DTOLoadSucceed loadSimulationFromFile(DTOLoadFile dto);

    ResultInfo runSimulation(DTOExecutionData dtoExecutionData);

    void resetEngine();

    StartData getSimulationStartData();

    boolean getIsSimulationLoaded();

    void saveState(String path);

    void loadState(String path);
}
