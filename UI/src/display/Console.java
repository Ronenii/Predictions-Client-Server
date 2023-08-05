package display;

/**
 * A class designated entirely for printing the relevant text of the program.
 */
public class Console {
    public static void printMainMenu() {
        System.out.println("1. Load simulation from file");
        System.out.println("2. Show simulation details");
        System.out.println("3. Run simulation");
        System.out.println("4. Show full details of past simulation run");
        System.out.println("5. Exit");
        System.out.print("Please enter your choice: ");
    }

    /**
     * Receives a string of simulation's details, formats it and prints it.
     */
    public static void showSimulationDetails(String simDetails) {
        // TODO: Implement this
    }


    /**
     * Prints out all past simulation details in short format.
     * @param simDetails - an array of all the past simulation details.
     */
    public static void showShortDetailsOfAllPastSimulations(String... simDetails) {
        for(int i = 0; i < simDetails.length; i++){
            System.out.print("${i}# ");
            printSimulationDetailsShort(simDetails[i]);
        }
    }


    /**
     * Shows a short representation of a simulation.
     * Short representation includes: id, name, date.
     * @param simDetails
     */
    public static void printSimulationDetailsShort(String simDetails) {
        // TODO: Implement this
    }
}
