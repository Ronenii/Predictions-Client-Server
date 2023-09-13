package manager;

import display.Console;
import engine2ui.simulation.execution.SetResponse;
import engine2ui.simulation.execution.StartResponse;
import engine2ui.simulation.genral.impl.objects.DTOEntity;
import engine2ui.simulation.genral.impl.objects.DTOEntityInstance;
import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.load.success.DTOLoadSucceed;
import engine2ui.simulation.result.ResultData;
import engine2ui.simulation.result.ResultInfo;
import manager.exception.SimulationNotLoadedException;
import ui2engine.simulation.execution.user.input.EntityPopulationUserInput;
import ui2engine.simulation.execution.user.input.EnvPropertyUserInput;
import ui2engine.simulation.load.DTOLoadFile;
import engine2ui.simulation.genral.impl.properties.DTOEnvironmentVariable;
import engine2ui.simulation.genral.impl.properties.StartData;
import input.Input;
import manager.options.ResultDisplayOptions;
import ui2engine.simulation.execution.DTOExecutionData;
import manager.validator.exceptions.IllegalBooleanValueException;
import manager.validator.exceptions.IllegalStringValueException;
import manager.validator.exceptions.OutOfRangeException;
import manager.validator.validator.InputValidator;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Responsible for UI communication with the Engine module. Sends data to the Engine, receives results from Engine accordingly,
 * Prints the data out using the Console class.
 */
public class EngineAgent {
    private final EngineInterface engine;

    private boolean isFileLoaded;

    public EngineAgent() {
        isFileLoaded = false;
        this.engine = new SimulationManager();
    }

    public boolean isFileLoaded() {
        return isFileLoaded;
    }

    /**
     * Gets the current simulation details from the engine and prints it.
     */
    public void showCurrentSimulationDetails() throws SimulationNotLoadedException {
        if (!engine.getIsSimulationLoaded()) {
            throw new SimulationNotLoadedException("There is no simulation loaded in the system.");
        }
        //Console.showSimulationDetails(engine.getCurrentSimulationDetails());
    }

    /**
     * prompts the user to input a path to a simulation XML config file and loads it
     * into the system.
     */
    public void loadSimulationFromFile(File file, List<EventListener> listeners) {
        DTOLoadSucceed dtoLoadSucceed = engine.loadSimulationFromFile(new DTOLoadFile(file, listeners));

        /* If we succeeded in creating the simulation we want to reset the engine.
         If a simulation was loaded beforehand and the creation failed we don't want
         to delete past data until a new simulation is loaded successfully. */

        // TODO: Change this to a graphical user notification
        if (dtoLoadSucceed.isSucceed()) {
            isFileLoaded = true;
            Console.println("The simulation creation has completed successfully");
            engine.resetEngine();
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
        DTOExecutionData dtoExecutionData = createDTOThirdFunctionObject(startData);

        ResultInfo resultInfo = engine.runSimulation(dtoExecutionData);
        Console.printSimulationResultInfo(resultInfo);
    }

    /**
     * This method gets from the user input for each environment property given from the 'StartData'.
     *
     * @param startData The DTO object from the engine that contains information about the environment properties.
     * @return a DTOThirdFunction object to send to the engine.
     */
    private DTOExecutionData createDTOThirdFunctionObject(StartData startData) {
        List<DTOEnvironmentVariable> environmentVariables = startData.getEnvironmentVariables();
        DTOExecutionData ret = new DTOExecutionData();
        Object valueToSend;
        String input;

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
            if (value.isEmpty()) {
                break;
            }

            try {
                switch (dtoEnvironmentVariable.getType()) {
                    case "decimal":
                        int integerValue = Integer.parseInt(value);
                        inputValidator.isIntegerInRange(integerValue, (int) dtoEnvironmentVariable.getFrom().doubleValue(), (int) dtoEnvironmentVariable.getTo().doubleValue());
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
    public void showPastSimulationResults() {
        ResultData[] pastSimulationsResultData = engine.getPastSimulationResultData();
        Console.showShortDetailsOfAllPastSimulations(pastSimulationsResultData);

        // Break out if there is no sim data to display.
        if (pastSimulationsResultData.length != 0) {
            if (pastSimulationsResultData.length == 1) {
                Console.println("Only one result to display.\n");
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

        // If there is only one simulation, there is no need to ask the user what simulation
        // he wants to display.
    }

    /**
     * Prompts the user for how to display the results, either by quantity of an entity,
     * or a histogram of a property.
     *
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
     *
     * @param entities The entities to get the histogram from
     */
    private void makeAndDisplayHistogram(DTOEntity[] entities) {
        DTOEntity entity = getEntityToDisplayHistogram(entities);
        if (entity.getInstances().length == 0) {
            Console.println("There are no living instances of the chosen entity.");
            return;
        }

        DTOProperty property = getPropertyToDisplayHistogram(entity);
        Map<Object, Integer> histogram = createHistogram(property, entity);

        Console.displayHistogramByType(histogram, property);
    }

    /**
     * Shows the user all entity types and gets user input for one of those entities.
     * Returns the chosen entity.
     *
     * @param entities The entities to get the histogram from
     */
    private DTOEntity getEntityToDisplayHistogram(DTOEntity[] entities) {
        Console.printEntityNames(entities);
        if (entities.length == 1) {
            Console.println("There is only one entity to display.");
            return entities[0];
        } else {
            int entityNumber = Input.getIntInputForListedItem("Choose the entity you wish to display", entities.length);
            return entities[entityNumber - 1];
        }
    }

    /**
     * Gets the path to the save file from the user.
     * serializes from said path into the engine
     */
    public void loadSimulationFromSaveState() {
        String path = Input.getInput("Please enter the full path the save file");
        File file = new File(path);
        if (!file.exists()) {
            Console.println("The path provided is invalid. Please provide the full path for the save file.");
        } else {
            engine.loadState(path);
            Console.println("The file was loaded successfully.");
        }
    }

    /**
     * Gets the path to save from the user.
     * gets the file location to save to.
     * deserializes the engine into the given path & file.
     */
    public void saveSimulationState() {
        String path, filename;
        path = Input.getInput("Please enter the path you want to save in");
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("The path provided is invalid. Please provide a valid path for saving the file.");
        } else {
            filename = Input.getInput("Please enter the filename of the save");
            if (!isValidFilename(filename)) {
                throw new IllegalArgumentException("The file name contains illegal characters. Please provide a valid name for saving the file.");
            }
            else{
                path += "\\" + filename + ".out";
                engine.saveState(path);
                Console.println("The file was saved successfully.");
            }
        }
    }

    public SetResponse sendPopulationData(EntityPopulationUserInput input){
        return engine.setEntityPopulation(input);
    }

    public SetResponse sendEnvironmentVariableData(EnvPropertyUserInput input){
        return engine.setEnvironmentVariable(input);
    }

    private static boolean isValidFilename(String filename) {
        // Check for empty filename
        if (filename.trim().isEmpty()) {
            return false;
        }

        // Check for invalid characters (e.g., Windows reserved characters)
        String invalidChars = "\\/:*?\"<>|";
        for (char c : filename.toCharArray()) {
            if (invalidChars.indexOf(c) != -1) {
                return false;
            }
        }

        // Check for reserved system filenames
        File file = new File(filename);
        try {
            if (!file.getCanonicalFile().getName().equals(filename)) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Shows the user all entity types and gets user input for one of those entities.
     * Returns the chosen entity.
     *
     * @param entity The entity to get the properties from and display a histogram.
     */
    private DTOProperty getPropertyToDisplayHistogram(DTOEntity entity) {
        Console.printEntityProperties(entity);
        if (entity.getProperties().length == 1) {
            Console.println("There is only one property to display.");
            return entity.getProperties()[0];
        } else {
            int propertyNumber = Input.getIntInputForListedItem("Choose the property you wish to see a histogram of", entity.getProperties().length);
            return entity.getProperties()[propertyNumber - 1];
        }
    }


    /**
     * Creates a histogram from the given property and entity's instances.
     * Sorts the histogram by key in ascending order.
     *
     * @param dtoProperty The property to build a histogram of
     * @param entity      We use this to go over all living instances of this entity and extract
     *                    the property's info.
     * @return A histogram map where the key is the value of the property, and the map's value
     * is the number of times the key appears.
     */
    private Map<Object, Integer> createHistogram(DTOProperty dtoProperty, DTOEntity entity) {
        Map<Object, Integer> unsortedMap = new HashMap<>();
        String propertyOfHistogram = dtoProperty.getName();

        for (DTOEntityInstance e : entity.getInstances()
        ) {
            Object value = e.getDTOPropertyByName(propertyOfHistogram).getValue();
            if (!unsortedMap.containsKey(value)) {
                unsortedMap.put(value, 1);
            } else {
                unsortedMap.put(value, unsortedMap.get(value) + 1);
            }
        }

        // This makes a new map with its keys sorted.
        return new TreeMap<>(unsortedMap);
    }

    public StartResponse startSimulation(){
        return engine.startSimulation();
    }
}
