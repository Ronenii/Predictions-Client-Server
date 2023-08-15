package display;

import engine2ui.simulation.start.DTOEnvironmentVariable;

import engine2ui.simulation.result.ResultData;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

/**
 * A class designated entirely for printing the relevant text of the program.
 */
public class Console {
    public static void printMainMenu() {
        printTitle("MAIN MENU");
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
     * Prints out the basic details of all past simulation saved in the system.
     *
     * @param pastSimulationsData - an array containing all result data from the currently loaded simulation
     *                            up to this point.
     */
    public static void showShortDetailsOfAllPastSimulations(ResultData[] pastSimulationsData) {

        // Sort the Result data by date time
        Arrays.sort(pastSimulationsData, (r1, r2)-> {
            return r1.getDateTime().compareTo(r2.getDateTime());
        });

        printTitle("PREVIOUS SIMULATION RUNS");
        System.out.println();
        System.out.printf("%-22s%-22s%-22s\n", "no.", "ID", "Date & Time");
        int counter = 1;
        for (ResultData r : pastSimulationsData
        ) {
           System.out.printf("#%-21s%-22s%-22s\n", counter++, r.getId(), r.getDateTimeString());
        }
    }

    /**
     * prints A title in the following format ##### TITLE #####
     * @param title the given string to print as a title
     */
    private static void printTitle(String title){
        System.out.printf("\n##### %s #####%n", title.toUpperCase());
    }


    /**
     * Shows a short representation of a simulation.
     * Short representation includes: id, name, date.
     *
     * @param simDetails
     */
    public static void printSimulationDetailsShort(String simDetails) {
        // TODO: Implement this
    }

    public static void printGivenMessage(String message){
        System.out.println(message);
    }

    public static void promptUserToInputPathForFile(){}
    //TODO: Implement this after we have the structure of a simulation's result data
    public static void printResultData(ResultData resultData)
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
