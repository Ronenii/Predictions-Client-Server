package manager;

import server2client.simulation.execution.SetResponse;
import server2client.simulation.execution.StartResponse;
import server2client.simulation.load.success.DTOLoadResult;
import server2client.simulation.genral.impl.properties.StartData;
import server2client.simulation.runtime.SimulationRunData;
import client2server.simulation.control.bar.DTOSimulationControlBar;

import server2client.simulation.runtime.ResultData;
import client2server.simulation.execution.user.input.EntityPopulationUserInput;
import client2server.simulation.execution.user.input.EnvPropertyUserInput;
import client2server.simulation.load.DTOLoadFile;

import java.io.File;

public interface EngineInterface {

    String getSimulationDetailsById(int simId);

    ResultData[] getPastSimulationResultData();

    DTOLoadResult loadSimulationFromFile(File file);

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
