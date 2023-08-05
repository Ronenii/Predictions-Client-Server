package manager;

import display.Console;
import manger.WorldManager;

/**
 * Responsible for UI communication with the Engine module. Sends data to the Engine, receives results from Engine accordingly,
 * Prints the data out using the Console class.
 */
public class EngineAgent {
    private WorldManager worldManager;

    /**
     * Gets the current simulation details from the engine and prints it.
     */
    private void showSimulationDetails(int simId) {
        // TODO: Make a getSimulationString function based on id
        String simDetails = "PLACEHOLDER";
        Console.showSimulationDetails(simDetails);
    }

    /**
     * Asks the engine to create a String describing the Current simulation details and returns it.
     * @return The Current Simulation Details.
     */
    public void showCurrentSimulationDetails() {
        // TODO: get the currents simulation's ID
        int currSimId = 0; // PLACEHOLDER
        showSimulationDetails(currSimId);
    }

    /**
     * prompts the user to input a path to a simulation XML config file and loads it
     * into the system.
     */
    public void loadSimulationFromFile() {
        // TODO: Implement this
    }


    /**
     * Starts a run of the currently loaded simulation.
     */
    public void runSimulation() {
        // TODO: Implement this
    }


    /**
     * Shows the user all past simulation (brief and simple representation: ID, Date, name).
     * Prompts the user to choose a simulation he wants to see the full details of (Based on ID).
     * Shows the user's chosen simulation details.
     */
    public void showPastSimulations() {
        Console.showShortDetailsOfAllPastSimulations();

        // TODO: Prompt user to choose a past simulation
        // TODO: Validate user choice
        // TODO: If valid, show user past simulation details
    }
}
