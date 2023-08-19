package manager;

import display.Console;
import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.objects.DTOEntityInstance;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.result.ResultData;
import manager.exception.SimulationNotLoadedException;
import ui2engine.simulation.func1.DTOFirstFunction;
import engine2ui.simulation.start.DTOEnvironmentVariable;
import engine2ui.simulation.start.StartData;
import input.Input;
import manager.options.ResultDisplayOptions;
import ui2engine.simulation.func3.DTOThirdFunction;
import validator.ui.exceptions.IllegalBooleanValueException;
import validator.ui.exceptions.IllegalStringValueException;
import validator.ui.exceptions.OutOfRangeException;
import validator.ui.validator.InputValidator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    public void showCurrentSimulationDetails() {
        Console.showSimulationDetails(engine.getCurrentSimulationDetails());
    }

    /**
     * prompts the user to input a path to a simulation XML config file and loads it
     * into the system.
     */
    public void loadSimulationFromFile() {
        System.out.print("Please enter path to the XML world config file: ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        DTOLoadSucceed dtoLoadSucceed;

        dtoLoadSucceed = engine.loadSimulationFromFile(new DTOFirstFunction(path));
        if (dtoLoadSucceed.isSucceed()) {
            Console.println("The simulation creation has completed successfully");
        } else {
            Console.println("The simulation creation has failed");
        }
    }


    /**
     * Starts a run of the currently loaded simulation.
     */
    public void runSimulation() throws SimulationNotLoadedException {
        if (!engine.getIsSimulationLoaded()) {
            throw new SimulationNotLoadedException("There is no simulation loaded in the system.");
        }

        StartData startData = engine.getSimulationStartData();
        DTOThirdFunction dtoThirdFunction = createDTOThirdFunctionObject(startData);

        engine.runSimulation(dtoThirdFunction);
    }

    /**
     * This method gets from the user input for each environment property given from the 'StartData'.
     *
     * @param startData The DTO object from the engine that contains information about the environment properties.
     * @return a DTOThirdFunction object to send to the engine.
     */
    private DTOThirdFunction createDTOThirdFunctionObject(StartData startData) {
        List<DTOEnvironmentVariable> environmentVariables = startData.getEnvironmentVariables();
        DTOThirdFunction ret = new DTOThirdFunction();
        Object valueToSend;
        String input;
        //TODO: add validation, check if the user enter a number and check if the number is in the range.

        for (DTOEnvironmentVariable dtoEnvironmentVariable : environmentVariables) {
            Console.showEnvPropertyDet(dtoEnvironmentVariable);
            Console.printPromptForEnvironmentPropertyInput(dtoEnvironmentVariable);
            input = Input.getInput();
            if (input.isEmpty()) {
                ret.updateEnvPropertyUserInputs(dtoEnvironmentVariable.getName(), getIsRandomInit(null), null);
            } else {
                valueToSend = tryToParse(input, dtoEnvironmentVariable);
                ret.updateEnvPropertyUserInputs(dtoEnvironmentVariable.getName(), getIsRandomInit(valueToSend), valueToSend);
            }
        }

        return ret;
    }

    /**
     * Return true if the value is null, that's how the engine would know to random a value for a specific environment property.
     */
    private boolean getIsRandomInit(Object value) {
        return value == null;
    }

    /**
     * Try to parse the input value from the user and check if the input is valid for the given environment property.
     * If the input value is not valid, the user will try to enter new value.
     *
     * @param value                  the user input.
     * @param dtoEnvironmentVariable a DTO object represent an environment property.
     * @return the given value parsed.
     */
    private Object tryToParse(String value, DTOEnvironmentVariable dtoEnvironmentVariable) {
        InputValidator inputValidator = new InputValidator();
        boolean valueIsNotValid = true;
        Object ret = null;

        while (valueIsNotValid) {
            // If after an error the user decide to random initialize the value.
            if (value.equals("\n")) {
                break;
            }

            try {
                switch (dtoEnvironmentVariable.getType()) {
                    case "decimal":
                        int integerValue = Integer.parseInt(value);
                        inputValidator.isIntegerInRange(integerValue, (int) dtoEnvironmentVariable.getFrom(), (int) dtoEnvironmentVariable.getTo());
                        ret = integerValue;
                        break;
                    case "float":
                        double doubleValue = Double.parseDouble(value);
                        inputValidator.isDoubleInRange(doubleValue, dtoEnvironmentVariable.getFrom(), dtoEnvironmentVariable.getTo());
                        ret = doubleValue;
                        break;
                    case "boolean":
                        inputValidator.validateBoolean(value);
                        ret = Boolean.parseBoolean(value);
                        break;
                    case "string":
                        inputValidator.validateStringValue(value);
                        ret = value;
                        break;
                }

                valueIsNotValid = false;

            } catch (IllegalStringValueException e) {
                Console.print(e.getMessage());
            } catch (OutOfRangeException e) {
                Console.print("The number is out of the property range! Please try again: ");
            } catch (NumberFormatException e) {
                Console.print(String.format("The input type can only be %s! Please try again: ", dtoEnvironmentVariable.getType()));
            } catch (IllegalBooleanValueException e) {
                Console.print("The property's value type is boolean! Please try again: ");
            } finally { // If an error occurred, the user will enter a new input.
                if (valueIsNotValid) {
                    value = Input.getInput();
                }
            }
        }

        return ret;
    }


    /**
     * Shows the user all past simulation (brief and simple representation: ID, Date, name).
     * Prompts the user to choose a simulation he wants to see the full details of (Based on ID).
     * Shows the user's chosen simulation details.
     */
    public void ShowPastSimulationResults() {
        ResultData[] pastSimulationsResultData = engine.getPastSimulationResultData();
        Console.showShortDetailsOfAllPastSimulations(pastSimulationsResultData);

        // Break out if there is no sim data to display.
        if (pastSimulationsResultData.length == 0) {
        }

        // If there is only one simulation, there is no need to ask the user what simulation
        // he wants to display.
        else if (pastSimulationsResultData.length == 1) {
            Console.println("Only one result to display.");
            chooseHowToDisplayResult(pastSimulationsResultData[0]);
        }

        // Get user input for the simulation run he wants to display.
        // Get user input on how he wants to display it.
        // Display the user chosen simulation run, in the way he chose it.
        else {
            int simResultNumber = Input.getIntInputForListedItem("Choose the simulation you wish to display", pastSimulationsResultData.length);
            chooseHowToDisplayResult((pastSimulationsResultData[simResultNumber - 1]));
        }
    }

    /**
     * Prompts the user for how to display the results, either by quantity of an entity,
     * or a histogram of a property.
     * @param resultData the results from a simulation.
     */
    private void chooseHowToDisplayResult(ResultData resultData) {
        Console.printResultDisplayOptionsMenu();
        int input = Input.getIntInputForListedItem("Choose you display preference", ResultDisplayOptions.values().length);
        ResultDisplayOptions resultDisplayOption = ResultDisplayOptions.values()[input - 1];

        switch (resultDisplayOption) {
            case QUANTITY:
                Console.printEntityPopulations(resultData.getEntities());
                break;
            case HISTOGRAM:
                makeAndDisplayHistogram(resultData.getEntities());
                break;
        }
    }

    /**
     * Makes a histogram based on user choice of entity and a property of said entity.
     * Prints this histogram.
     * @param entities The entities to get the histogram from
     */
    private void makeAndDisplayHistogram(DTOEntity[] entities) {
        DTOEntity entity = getEntityToDisplayHistogram(entities);
        if(entity.getInstances().length == 0){
            Console.println("There are no living instances of the chose entity.");
        }

        DTOProperty property = getPropertyToDisplayHistogram(entity);
        Map<Object, Integer> histogram = createHistogram(property, entity);

        Console.displayHistogramByType(histogram,property);
    }

    /**
     * Shows the user all entity types and gets user input for one of those entities.
     * Returns the chosen entity.
     * @param entities The entities to get the histogram from
     */
    private DTOEntity getEntityToDisplayHistogram(DTOEntity[] entities) {
        Console.printEntityNames(entities);
        int entityNumber = Input.getIntInputForListedItem("Choose the entity you wish to display", entities.length);
        return entities[entityNumber - 1];
    }

    /**
     * Shows the user all entity types and gets user input for one of those entities.
     * Returns the chosen entity.
     * @param entity The entity to get the properties from and display a histogram.
     */
    private DTOProperty getPropertyToDisplayHistogram(DTOEntity entity) {
        Console.printEntityProperties(entity);
        int propertyNumber = Input.getIntInputForListedItem("Choose the property you wish to see a histogram of", entity.getProperties().length);
        return entity.getProperties()[propertyNumber - 1];
    }


    /**
     * Creates a histogram from the given property and entity's instances.
     * @param dtoProperty The property to build a histogram of
     * @param entity We use this to go over all living instances of this entity and extract
     *               the property's info.
     * @return A histogram map where the key is the value of the property, and the map's value
     * is the number of times the key appears.
     */
    private Map<Object, Integer> createHistogram(DTOProperty dtoProperty, DTOEntity entity) {
        Map<Object, Integer> ret = new LinkedHashMap<>();
        String propertyOfHistogram = dtoProperty.getName();

        for (DTOEntityInstance e : entity.getInstances()
        ) {
            Object value = e.getDTOPropertyByName(propertyOfHistogram).getValue();
            if (!ret.containsKey(value)) {
                ret.put(value, 1);
            } else {
                ret.put(value, ret.get(value) + 1);
            }
        }

        return ret;
    }
}
