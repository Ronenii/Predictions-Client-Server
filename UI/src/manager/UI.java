package manager;

import display.Console;
import input.Input;
import manager.exception.SimulationNotLoadedException;
import manager.options.MenuOptions;

import java.util.Scanner;

/**
 * The class the controls the main program loop. handles getting input from user, sends it to the engineAgent to handle it.
 */
public class UI {
    private boolean exit;
    private EngineAgent engineAgent;

    public UI() {
        exit = false;
    }

    /**
     * The main program loop. All exceptions are handled here.
     */
    public void runProgram() {
        engineAgent = new EngineAgent();
        while (!exit) {
            Console.printMainMenu();

            try {
                handleUserMenuChoice();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Prompts user to input an argument for the menu and returns it as a MenuOption enum.
     *
     * @return the menu item chosen by the user
     */
    public MenuOptions getMenuInput() {
        MenuOptions userInput;

        userInput = MenuOptions.values()[Input.getIntInputForListedItem("Please enter your choice", MenuOptions.values().length) - 1];
        return userInput;
    }

    /**
     * Gets the user's menu input and handles it accordingly.
     * Handles incorrect user input.
     */
    public void handleUserMenuChoice() throws SimulationNotLoadedException {
        MenuOptions menuOption = getMenuInput();

        switch (menuOption) {
            case LOAD_PROGRAM_FROM_XML:
                // C:\Users\Ronen Gelmanovich\IdeaProjects\Predictions\WorldConfigFiles\ex1-cigarets.xml
                // C:\Users\Ronen Gelmanovich\IdeaProjects\Predictions\WorldConfigFiles\master-ex1.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-cigarets.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\master-ex1.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-error-2.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-error-4.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-error-6.xml
                engineAgent.loadSimulationFromFile();
                break;
            case LOAD_PROGRAM_STATE:
                engineAgent.loadSimulationFromSaveState();
                break;
            case SAVE_PROGRAM_STATE:
                engineAgent.saveSimulationState();
                break;
            case SHOW_SIMULATION_DATA:
                engineAgent.showCurrentSimulationDetails();
                break;
            case RUN_SIMULATION:
                engineAgent.runSimulation();
                break;
            case SHOW_PAST_SIMULATIONS:
                engineAgent.showPastSimulationResults();
                break;
            case EXIT:
                exit = true;
                break;
            default:
                throw new IllegalArgumentException("Input not listed on menu.");
        }
    }
}
