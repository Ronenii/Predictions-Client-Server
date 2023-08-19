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

    public UI(){
        exit = false;
    }

    /**
     * The main program loop. All exceptions are handled here.
     */
    public void runProgram()
    {
        engineAgent = new EngineAgent();

        while(!exit)
        {
            Console.printMainMenu();

            try {
                handleUserMenuChoice();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Magniv");
    }

    /**
     * Prompts user to input an argument for the menu and returns it as a MenuOption enum.
     *
     * @return the menu item chosen by the user
     */
    public MenuOptions getMenuInput()
    {
        MenuOptions userInput;
        Scanner scanner = new Scanner(System.in);

        userInput = MenuOptions.values()[Input.getIntInputForListedItem("Please enter your choice", MenuOptions.values().length)-1];
        return userInput;
    }

    /**
     * Gets the user's menu input and handles it accordingly.
     * Handles incorrect user input.
     */
    public void handleUserMenuChoice() throws SimulationNotLoadedException {
        MenuOptions menuOption = getMenuInput();

        switch (menuOption)
        {
            case LOAD_PROGRAM:
                // C:\Users\Ronen Gelmanovich\IdeaProjects\Predictions\WorldConfigFiles\ex1-cigarets.xml
                // C:\Users\Ronen Gelmanovich\IdeaProjects\Predictions\WorldConfigFiles\master-ex1.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-cigarets.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\master-ex1.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-error-2.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-error-4.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-error-6.xml
                engineAgent.loadSimulationFromFile();
                break;
            case SHOW_SIMULATION_DATA:
                engineAgent.showCurrentSimulationDetails();
                break;
            case RUN_SIMULATION:
                engineAgent.runSimulation();
                break;
            case SHOW_PAST_SIMULATIONS:
                engineAgent.ShowPastSimulationResults();
                break;
            case EXIT:
                exit = true;
                break;
            default:
                throw new IllegalArgumentException("Input not listed on menu.");
        }
    }
}
