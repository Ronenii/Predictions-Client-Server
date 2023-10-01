package manager;

import engine2ui.simulation.execution.SetResponse;
import engine2ui.simulation.execution.StartResponse;
import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.result.ResultInfo;
import engine2ui.simulation.genral.impl.properties.StartData;
import engine2ui.simulation.runtime.SimulationRunData;
import ui2engine.simulation.control.bar.DTOSimulationControlBar;
import ui2engine.simulation.execution.DTOExecutionData;

import engine2ui.simulation.runtime.ResultData;
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;
import ui2engine.simulation.execution.user.input.EnvPropertyUserInput;
import ui2engine.simulation.load.DTOLoadFile;

public interface EngineInterface {

    String getSimulationDetailsById(int simId);

    ResultData[] getPastSimulationResultData();

    DTOLoadSucceed loadSimulationFromFile(DTOLoadFile dto);

    StartResponse startSimulation();

    void resetEngine();

    StartData getSimulationStartData();

    boolean getIsSimulationLoaded();

    void saveState(String path);

    void loadState(String path);

    SetResponse setEntityPopulation(EntityPopulationUserInput input);
    SetResponse setEnvironmentVariable(EnvPropertyUserInput input);

    SimulationRunData getRunDataById(String simId);

    void shutdownThreadPool();
    void setStopPausePlayOrSkipFwdForSimById(String simId, DTOSimulationControlBar dtoSimulationControlBar);
}
