package manager;

import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.prview.PreviewData;
import engine2ui.simulation.result.ResultInfo;
import engine2ui.simulation.start.StartData;
import ui2engine.simulation.func3.DTOThirdFunction;

import engine2ui.simulation.result.ResultData;
import ui2engine.simulation.load.DTOLoad;

public interface EngineInterface {
    PreviewData getCurrentSimulationDetails();

    String getSimulationDetailsById(int simId);

    ResultData[] getPastSimulationResultData();

    DTOLoadSucceed loadSimulationFromFile(DTOLoad dto);

    ResultInfo runSimulation(DTOThirdFunction dtoThirdFunction);

    void resetEngine();

    StartData getSimulationStartData();

    boolean getIsSimulationLoaded();

    void saveState(String path);

    void loadState(String path);
}
