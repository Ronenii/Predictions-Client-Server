package manager;

import engine2ui.simulation.result.ResultData;

public interface EngineInterface {
    String getCurrentSimulationDetails();

    String getSimulationDetailsById(int simId);

    ResultData[] getPastSimulationResultData();

    void loadSimulationFromFile(String path);

    void runSimulation();
}
