package manager;

public interface EngineInterface {
    public String getCurrentSimulationDetails();

    public String getSimulationDetailsById(int simId);

    public String[] getAllSimulationDetailsInShortFormat();

    public void loadSimulationFromFile(String path);

    public void runSimulation();
}
