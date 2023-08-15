package display;

import engine2ui.simulation.start.DTOEnvironmentVariable;

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

    public static void printGivenMessage(String message){
        System.out.println(message);
    }

    public static void promptUserToInputPathForFile()
    {
        System.out.print("Please enter path to the XML world config file: ");
    }

    public static void showThirdFuncFirstMessage(){
        System.out.println("For each environment value in the simulation, please choose if you want to initialize the value manually or randomly.");
        System.out.println("If you want to initialize the value manually, please enter your value.");
        System.out.println("If you want to initialize the value randomly, please press enter.");
    }

    public static void showEnvPropertyDet(DTOEnvironmentVariable dtoEnvironmentVariable){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'").append(dtoEnvironmentVariable.getName()).append("':\n");
        stringBuilder.append("Type: ").append(dtoEnvironmentVariable.getType()).append("\n");
        if (dtoEnvironmentVariable.getType().equals("int")){
            stringBuilder.append("Value range: from ")
                    .append((int)dtoEnvironmentVariable.getFrom())
                    .append(" to ").append((int)dtoEnvironmentVariable.getTo())
                    .append("\n");
        } else if (dtoEnvironmentVariable.getType().equals("double")){
            stringBuilder.append("Value range: from ")
                    .append(dtoEnvironmentVariable.getFrom())
                    .append(" to ").append(dtoEnvironmentVariable.getTo())
                    .append("\n");
        }

        stringBuilder.append("========================");
        System.out.println(stringBuilder.toString());
    }
}
