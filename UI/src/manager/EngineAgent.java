package manager;

import display.Console;
import engine2ui.simulation.result.ResultData;
import ui2engine.simulation.func1.DTOFirstFunction;
import engine2ui.simulation.start.DTOEnvironmentVariable;
import engine2ui.simulation.start.StartData;
import input.Input;
import ui2engine.simulation.func3.DTOThirdFunction;
import ui2engine.simulation.func3.user.input.EnvPropertyUserInput;
import validator.ui.exceptions.IllegalBooleanValueException;
import validator.ui.exceptions.IllegalStringValueException;
import validator.ui.exceptions.OutOfRangeException;
import validator.ui.validator.InputValidator;

import java.util.List;

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
    public void loadSimulationFromFile() {
        System.out.print("Please enter path to the XML world config file: ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();

        engine.loadSimulationFromFile(new DTOFirstFunction(path));
    }


    /**
     * Starts a run of the currently loaded simulation.
     */
    public void runSimulation() {
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
    private DTOThirdFunction createDTOThirdFunctionObject(StartData startData){
        List<DTOEnvironmentVariable> environmentVariables = startData.getEnvironmentVariables();
        DTOThirdFunction ret = new DTOThirdFunction();
        Object valueToSend;
        String input;

        Console.showThirdFuncFirstMessage();
        for(DTOEnvironmentVariable dtoEnvironmentVariable : environmentVariables){
            Console.showEnvPropertyDet(dtoEnvironmentVariable);
            input = Input.getInput();
            if (input.equals("\n")){
                ret.updateEnvPropertyUserInputs(dtoEnvironmentVariable.getName(),getIsRandomInit(null), null);
            }
            else {
                valueToSend = tryToParse(input, dtoEnvironmentVariable);
                ret.updateEnvPropertyUserInputs(dtoEnvironmentVariable.getName(), getIsRandomInit(valueToSend), valueToSend);
            }
        }

        return ret;
    }

    /**
     * Return true if the value is null, that's how the engine would know to random a value for a specific environment property.
     */
    private boolean getIsRandomInit(Object value){
        boolean ret = false;

        if(value == null){
            ret = true;
        }

        return ret;
    }

    /**
     * Try to parse the input value from the user and check if the input is valid for the given environment property.
     * If the input value is not valid, the user will try to enter new value.
     *
     * @param value the user input.
     * @param dtoEnvironmentVariable a DTO object represent an environment property.
     * @return the given value parsed.
     */
    private Object tryToParse(String value, DTOEnvironmentVariable dtoEnvironmentVariable){
        InputValidator inputValidator = new InputValidator();
        boolean valueIsNotValid = true;
        Object ret = null;

        while (valueIsNotValid) {
            // If after an error the user decide to random initialize the value.
            if(value.equals("\n")) {
                break;
            }

            try{
                switch (dtoEnvironmentVariable.getType()){
                    case "int":
                        int integerValue = Integer.parseInt(value);
                        inputValidator.isIntegerInRange(integerValue, (int)dtoEnvironmentVariable.getFrom(), (int)dtoEnvironmentVariable.getTo());
                        ret = integerValue;
                        break;
                    case "double":
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
                Console.printGivenMessage(e.getMessage());
            } catch (OutOfRangeException e) {
                Console.printGivenMessage("The number is out of the property range! Please try again.");
            } catch (NumberFormatException e){
                Console.printGivenMessage("The number type doesn't match the property's value type! Please try again.");
            } catch (IllegalBooleanValueException e) {
                Console.printGivenMessage("Thr property's value type is boolean! Please try again.");
            }
            finally { // If an error occurred, the user will enter a new input.
              if(valueIsNotValid){
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
    public void showPastSimulations() {
        ResultData[] pastSimulationsResultData = engine.getPastSimulationResultData();
        Console.showShortDetailsOfAllPastSimulations(pastSimulationsResultData);

        System.out.print("\nChoose the no. of a past run you would like to view: ");

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
