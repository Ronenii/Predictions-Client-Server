package manager;

import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.prview.PreviewData;
import engine2ui.simulation.result.ResultInfo;
import engine2ui.simulation.start.StartData;
import ui2engine.simulation.func3.DTOThirdFunction;

import engine2ui.simulation.result.ResultData;
import ui2engine.simulation.func1.DTOFirstFunction;

public interface EngineInterface {
    PreviewData getCurrentSimulationDetails();

    String getSimulationDetailsById(int simId);

    ResultData[] getPastSimulationResultData();

    DTOLoadSucceed loadSimulationFromFile(DTOFirstFunction dto);

    ResultInfo runSimulation(DTOThirdFunction dtoThirdFunction);

    StartData getSimulationStartData();

    boolean getIsSimulationLoaded();
}
