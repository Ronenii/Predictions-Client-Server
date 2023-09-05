package display;

import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.properties.DTOEndingCondition;
import engine2ui.simulation.genral.impl.properties.DTORule;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.genral.impl.properties.property.impl.RangedDTOProperty;
import engine2ui.simulation.prview.PreviewData;
import engine2ui.simulation.result.ResultInfo;
import engine2ui.simulation.start.DTOEnvironmentVariable;
import engine2ui.simulation.result.ResultData;
import simulation.properties.property.api.PropertyType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * A class designated entirely for printing the relevant text of the program.
 */
public class Console {
    public static void printMainMenu() {
        printTitle("MAIN MENU");
        System.out.println("1. Load simulation from xml configuration file");
        System.out.println("2. Load simulation from save state file");
        System.out.println("3. Save the state of the program");
        System.out.println("4. Show simulation details");
        System.out.println("5. Run simulation");
        System.out.println("6. Show details of past simulation run");
        System.out.println("7. Exit");
    }

    /**
     * Receives a string of simulation's details, formats it and prints it.
     */
    public static void showSimulationDetails(PreviewData previewData) {
        printTitle("SIMULATION DETAILS");
        printEntitiesDetails(previewData.getEntities());
        printRulesDetails(previewData.getRules());
        printEndingConditions(previewData.getEndingConditions());
    }


    /**
     * Prints out the basic details of all past simulation saved in the system.
     *
     * @param pastSimulationsData - an array containing all result data from the currently loaded simulation
     *                            up to this point.
     */
    public static void showShortDetailsOfAllPastSimulations(ResultData[] pastSimulationsData) {

        if (pastSimulationsData.length == 0) {
            System.out.println("No past simulations to display.");
            return;
        }
        // Sort the Result data by date time
        Arrays.sort(pastSimulationsData, Comparator.comparing(ResultData::getDateTime));

        printTitle("PREVIOUS SIMULATION RUNS");
        System.out.println();
        System.out.printf("%-22s%-22s%-22s\n", "no.", "ID", "Date & Time");
        int counter = 1;
        for (ResultData r : pastSimulationsData
        ) {
            System.out.printf("#%-21s%-22s%-22s\n", counter++, r.getId(), r.getDateTimeString());
        }
        println();
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

    public static void println(String message) {
        System.out.println(message);
    }

    public static void println() {
        System.out.println();
    }

    public static void print(String message) {
        System.out.print(message);
    }

    public static void printResultDisplayOptionsMenu() {
        System.out.println("Display results by:");
        System.out.println("1. Entity quantity");
        System.out.println("2. Histogram of property");
    }

    /**
     * Displays the histogram by the type of the property it represents.
     *
     * @param histogram The histogram to display
     * @param property  used for display and getting the property type's
     */
    public static void displayHistogramByType(Map<Object, Integer> histogram, DTOProperty property) {
        PropertyType type = PropertyType.valueOf(property.getType());

        System.out.printf("Histogram for property: %s\n\n", property.getName().toUpperCase());
        switch (type) {
            case DECIMAL:
                printHistogram(histogram, Integer.class);
                break;
            case FLOAT:
                printHistogram(histogram, Double.class);
                break;
            case BOOLEAN:
                printHistogram(histogram, Boolean.class);
                break;
            case STRING:
                printHistogram(histogram, String.class);
                break;
        }
    }

    /**
     * @param histogram    a map of keys and values where the keys are a property's value, and the
     *                     map's values are the quantity this value appears.
     * @param castingClass The class to cast the keys of the maps to.
     */
    private static void printHistogram(Map<Object, Integer> histogram, Class castingClass) {
        System.out.println("Value: Quantity");
        for (Object o : histogram.keySet()
        ) {
            System.out.printf("%s: %d\n", castingClass.cast(o), histogram.get(o));
        }
    }

    public static void printPromptForEnvironmentPropertyInput(DTOEnvironmentVariable dtoEnvironmentVariable) {
        System.out.print("\nInput the value of the environment variable ");
        switch (dtoEnvironmentVariable.getType()) {
            case "decimal":
                System.out.printf("(%d-%d)\n", (int) dtoEnvironmentVariable.getFrom().doubleValue(), (int) dtoEnvironmentVariable.getTo().doubleValue());
                break;
            case "float":
                System.out.printf("(%.2f-%.2f)\n", dtoEnvironmentVariable.getFrom(), dtoEnvironmentVariable.getTo());
                break;
            case "boolean":
                Console.print("(true|false)\n");
                break;
            case "string":
                Console.print("a string of up to 50 characters, can only contain: \n" +
                        "- lower and upper case letters.\n" +
                        "- numbers: 0-9\n" +
                        "- these special characters: 'space'!,?_-.()\n");
                break;
        }
        System.out.print("Or press enter for random value: ");
    }

    public static void showEnvPropertyDet(DTOEnvironmentVariable dtoEnvironmentVariable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----------------\n").append("Environment variable name: ").append("'").append(dtoEnvironmentVariable.getName()).append("'\n");
        stringBuilder.append("Type: ").append(dtoEnvironmentVariable.getType());
        System.out.println(stringBuilder);
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
        Console.println();
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
            System.out.printf("\n#%s", i);
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
        printTitle(entity.getName());
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

    private static void printEntitiesDetails(List<DTOEntity> entities) {
        printTitle("ENTITIES");

        for (DTOEntity dtoEntity : entities) {
            System.out.printf("\nName : %s\n", dtoEntity.getName());
            System.out.printf("Entity's population: %d\n", dtoEntity.getStartingPopulation());
            System.out.println("Properties: ");
            printEntityProperties(dtoEntity.getProperties());
        }
    }

    private static void printEntityProperties(DTOProperty[] properties) {
        RangedDTOProperty rangedDTOProperty;

        for (DTOProperty dtoProperty : properties) {
            System.out.printf("\tProperty name: %s\n", dtoProperty.getName());
            System.out.printf("\tProperty type: %s\n", dtoProperty.getType());
            System.out.printf("\tIs random initialized: %s\n", dtoProperty.isRandomInit());
            if (dtoProperty.getClass() == RangedDTOProperty.class) {
                rangedDTOProperty = (RangedDTOProperty) dtoProperty;
                if (dtoProperty.getType().equals("DECIMAL")) {
                    System.out.printf("\tProperty's range: from %d to %d\n\n", (int) rangedDTOProperty.getFrom(), (int) rangedDTOProperty.getTo());
                } else {
                    System.out.printf("\tProperty's range: from %.2f to %.2f\n\n", rangedDTOProperty.getFrom(), rangedDTOProperty.getTo());
                }
            } else {
                println();
            }
        }
    }

    private static void printRulesDetails(List<DTORule> rules) {
        printTitle("RULES");

        for (DTORule dtoRule : rules) {
            System.out.printf("\nName : %s\n", dtoRule.getName());
            System.out.printf("Ticks : %d\n", dtoRule.getTicks());
            System.out.printf("Probability : %.1f\n", dtoRule.getProbability());
            System.out.printf("Number of actions : %d\n", dtoRule.getActions().size());
            System.out.println("Actions types: ");
            System.out.print("\t");
            //System.out.println(getStringOfActionsNames(dtoRule.getActions()));

        }
    }

    private static String getStringOfActionsNames(String[] actions) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        int counter = 1, actionsCount = actions.length;

        for (String action : actions) {
            stringBuilder.append(action);
            if (actionsCount != counter) {
                stringBuilder.append(", ");
            }
            counter++;
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static void printEndingConditions(List<DTOEndingCondition> endingConditions) {
        printTitle("ENDING CONDITIONS");
        int counter = 1;

        for (DTOEndingCondition dtoEndingCondition : endingConditions) {
            System.out.printf("\nEnding condition #%d\n", counter);
            System.out.printf("Ending by: %s\n", dtoEndingCondition.getType());
            System.out.printf("Limit: %d\n", dtoEndingCondition.getCount());
            counter++;
        }
    }

    public static void printSimulationResultInfo(ResultInfo resultInfo) {
        System.out.println("\nSimulation run has completed.");
        System.out.printf("Simulation run id: %s\n", resultInfo.getId());
        System.out.printf("The simulation run terminated after %s %s.\n", resultInfo.getEndingCondition().getCount(), resultInfo.getEndingCondition().getType().toLowerCase());

    }
}
