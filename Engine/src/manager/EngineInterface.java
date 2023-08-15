package manager;

import engine2ui.simulation.start.StartData;
import ui2engine.simulation.func3.DTOThirdFunction;

public interface EngineInterface {
    String getCurrentSimulationDetails();

    String getSimulationDetailsById(int simId);

    String[] getAllSimulationDetailsInShortFormat();

    void loadSimulationFromFile(String path);

    void runSimulation(DTOThirdFunction dtoThirdFunction);

    StartData getSimulationStartData();
}
