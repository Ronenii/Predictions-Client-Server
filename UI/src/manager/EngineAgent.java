package manager;

import display.Console;
import engine2ui.simulation.result.ResultData;

import java.util.Scanner;

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
        ResultData[] pastSimulationsResultData = engine.getPastSimulationResultData();
        Console.showShortDetailsOfAllPastSimulations(pastSimulationsResultData);

        System.out.println("Choose the no. of a past run you would like to view: ");

        //TODO: PLACEHOLDER INPUT GETTER FROM USER
        Scanner scanner = new Scanner(System.in);
        int userInput = Integer.parseInt(scanner.nextLine());

        if(userInput >= 1 && userInput <= pastSimulationsResultData.length)
        {
            Console.printResultData(pastSimulationsResultData[userInput -1]);
        }

        //TODO: PLACEHOLDER INPUT GETTER FROM USER
    }
}
