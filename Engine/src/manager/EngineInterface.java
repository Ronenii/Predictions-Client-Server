package manager;

public interface EngineInterface {
    String getCurrentSimulationDetails();

    String getSimulationDetailsById(int simId);

    String[] getAllSimulationDetailsInShortFormat();

    void loadSimulationFromFile(String path);

    void runSimulation();
}
