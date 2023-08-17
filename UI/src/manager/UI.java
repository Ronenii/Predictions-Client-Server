package manager;

import display.Console;
import manager.options.MenuOptions;

import java.util.Scanner;

/**
 * The class the controls the main program loop. handles getting input from user, sends it to the engineAgent to handle it.
 */
public class UI {
    private boolean exit =false;
    private EngineAgent engineAgent;


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

        userInput = MenuOptions.values()[Integer.parseInt(scanner.nextLine()) - 1];

        return userInput;
    }

    /**
     * Gets the user's menu input and handles it accordingly.
     * Handles incorrect user input.
     */
    public void handleUserMenuChoice()
    {
        MenuOptions menuOption = getMenuInput();

        switch (menuOption)
        {
            case LOAD_PROGRAM:
                // C:\Users\Ronen Gelmanovich\IdeaProjects\Predictions\WorldConfigFiles\ex1-cigarets.xml
                // C:\Users\Roy\IdeaProjects\Predictions\WorldConfigFiles\ex1-cigarets.xml
                engineAgent.loadSimulationFromFile();
                break;
            case SHOW_SIMULATION_DATA:
                engineAgent.showCurrentSimulationDetails();
                break;
            case RUN_SIMULATION:
                engineAgent.runSimulation();
                break;
            case SHOW_PAST_SIMULATIONS:
                engineAgent.MenuOption4();
                break;
            case EXIT:
                exit = true;
                break;
            default:
                throw new IllegalArgumentException("Input not listed on menu.");
        }
    }
}
