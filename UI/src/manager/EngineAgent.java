package manager;

import display.Console;

/**
 * Responsible for UI communication with the Engine module. Sends data to the Engine, receives results from Engine accordingly,
 * Prints the data out using the Console class.
 */
public class EngineAgent {
    private final EngineInterface engine;

    public EngineAgent() {
        this.engine = new WorldManager();
    }

    /**
     * Gets the current simulation details from the engine and prints it.
     */
    private void showSimulationDetails(int simId) {
        Console.showSimulationDetails(engine.getSimulationDetailsById(simId));
    }

    /**
     * Asks the engine to create a String describing the Current simulation details and returns it.
     */
    public void showCurrentSimulationDetails() {
        Console.showSimulationDetails(engine.getCurrentSimulationDetails());
    }

    /**
     * prompts the user to input a path to a simulation XML config file and loads it
     * into the system.
     */
    public void loadSimulationFromFile(String Path) {
        engine.loadSimulationFromFile(Path);
    }


    /**
     * Starts a run of the currently loaded simulation.
     */
    public void runSimulation() {
        engine.runSimulation();
    }


    /**
     * Shows the user all past simulation (brief and simple representation: ID, Date, name).
     * Prompts the user to choose a simulation he wants to see the full details of (Based on ID).
     * Shows the user's chosen simulation details.
     */
    public void showPastSimulations() {
        Console.showShortDetailsOfAllPastSimulations(engine.getPastSimulationResultData());

        // TODO: Prompt user to choose a past simulation
        // TODO: Validate user choice
        // TODO: If valid, show user past simulation details
    }
}
