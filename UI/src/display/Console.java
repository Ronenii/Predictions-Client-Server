package display;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.start.DTOEnvironmentVariable;

import engine2ui.simulation.result.ResultData;

import java.util.Arrays;

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

        if (pastSimulationsData.length == 0) {
            System.out.println("No past simulations to display.\n");
            return;
        }
        // Sort the Result data by date time
        Arrays.sort(pastSimulationsData, (r1, r2) -> {
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
     *
     * @param title the given string to print as a title
     */
    private static void printTitle(String title) {
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

    public static void printGivenMessage(String message) {
        System.out.println(message);
    }

    public static void promptUserToInputPathForFile() {
    }

    //TODO: Implement this after we have the structure of a simulation's result data
    public static void printResultData(ResultData resultData) {
        Console.printTitle("ENTITIES");
        Console.printTitle("");
    }

    public static void printResultDisplayOptionsMenu() {
        System.out.println("Display results by:");
        System.out.println("1. Entity quantity");
        System.out.println("2. Histogram of property");
    }

    public static void showThirdFuncFirstMessage() {
        System.out.println("For each environment value in the simulation, please choose if you want to initialize the value manually or randomly.");
        System.out.println("If you want to initialize the value manually, please enter your value.");
        System.out.println("If you want to initialize the value randomly, please press enter.");
    }

    public static void showEnvPropertyDet(DTOEnvironmentVariable dtoEnvironmentVariable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'").append(dtoEnvironmentVariable.getName()).append("':\n");
        stringBuilder.append("Type: ").append(dtoEnvironmentVariable.getType()).append("\n");
        if (dtoEnvironmentVariable.getType().equals("int")) {
            stringBuilder.append("Value range: from ")
                    .append((int) dtoEnvironmentVariable.getFrom())
                    .append(" to ").append((int) dtoEnvironmentVariable.getTo())
                    .append("\n");
        } else if (dtoEnvironmentVariable.getType().equals("double")) {
            stringBuilder.append("Value range: from ")
                    .append(dtoEnvironmentVariable.getFrom())
                    .append(" to ").append(dtoEnvironmentVariable.getTo())
                    .append("\n");
        }

        stringBuilder.append("========================");
        System.out.println(stringBuilder.toString());
    }

    /**
     * Displays the given entity array in the following format:
     * ### ENTITIES ###
     * <p>
     * #1 entity0Name
     * #2 entity1Name
     * .
     * .
     * .
     *
     * @param entities the given entities we wish to see the names of
     */
    public static void printEntityNames(DTOEntity[] entities) {
        printTitle("ENTITIES");
        for (int i = 1; i <= entities.length; i++) {
            System.out.printf("#%s %s\n", i, entities[i - 1].getName());
        }
    }

    /**
     * Displays all entities numbered one by one, for each entity displays its starting population
     * and population when simulation ended. When all entities are displayed, displays the
     * sum of all the starting populations and the sum of all the ending populations.
     *
     * @param entities All entities to display.
     */
    public static void printEntityPopulations(DTOEntity[] entities) {
        int totalStartingPopulation = 0;
        int totalEndingPopulation = 0;

        for (int i = 1; i <= entities.length; i++) {
            totalStartingPopulation += entities[i - 1].getStartingPopulation();
            totalEndingPopulation += entities[i - 1].getEndingPopulation();
            System.out.printf("\n#%s\n", i);
            printEntityDetails(entities[i - 1]);
        }

        System.out.printf("\nTotal population when simulation started: %s\n", totalStartingPopulation);
        System.out.printf("Total population when simulation ended: %s\n", totalEndingPopulation);
    }

    /**
     * Prints out the given entity's details in the following format:
     * ENTITY
     * entity's starting population:
     * entity's population when simulation ended:
     *
     * @param entity the given entity to print.
     */
    private static void printEntityDetails(DTOEntity entity) {
        System.out.printf("%s\n", entity.getName().toUpperCase());
        System.out.printf("%s starting population: %s\n", entity.getName(), entity.getStartingPopulation());
        System.out.printf("%s population when simulation ended: %s\n", entity.getName(), entity.getEndingPopulation());
    }

    /**
     * Prints the given entity's properties in the following format.
     * <p>
     * ### ENTITY'S PROPERTIES ###
     * <p>
     * #1 property0Name-PROPERTY0TYPE
     * #2 property1Name-PROPERTY1TYPE
     * .
     * .
     * .
     *
     * @param entity the entity that we wish to print its properties.
     */
    public static void printEntityProperties(DTOEntity entity) {
        printTitle(String.format("%s'S PROPERTIES", entity.getName().toUpperCase()));
        DTOProperty[] properties = entity.getProperties();
        for (int i = 1; i <= properties.length; i++) {
            System.out.printf("# %s %s: %s\n", i, properties[i - 1].getType().toUpperCase(), properties[i - 1].getName());
        }
    }
}
