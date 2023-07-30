package manager;

import display.Console;
import manager.options.MenuOptions;

import java.util.Scanner;

public class UI {
    private static boolean exit =false;
    private static EngineAgent engineAgent;

    public static void main(String[] args) {
        runProgram();
    }

    /**
     * The main program loop. All exceptions are handled here.
     */
    public static void runProgram()
    {
        while(!exit)
        {
            Console.printMainMenu();
            try {
                handleUserMenuChoice();
            }
            catch (Exception e)
            {

            }
        }

        System.out.println("Magniv");
    }

    /**
     * Prompts user to input an argument for the menu and returns it as a MenuOption enum.
     *
     * @return the menu item chosen by the user
     */
    public static MenuOptions getMenuInput()
    {
        MenuOptions userInput;
        Scanner scanner = new Scanner(System.in);
        userInput = MenuOptions.valueOf(scanner.nextLine());

        return userInput;
    }

    /**
     * Gets the user's menu input and handles it accordingly.
     * Handles incorrect user input.
     */
    public static void handleUserMenuChoice()
    {
        MenuOptions menuOption = getMenuInput();

        switch (menuOption)
        {
            case LOAD_PROGRAM:
                engineAgent.loadSimulationFromFile();
                break;
            case SHOW_SIMULATION_DATA:
                engineAgent.showCurrentSimulationDetails();
                break;
            case RUN_SIMULATION:
                engineAgent.runSimulation();
                break;
            case SHOW_PAST_SIMULATIONS:
                engineAgent.showPastSimulations();
                break;
            case EXIT:
                exit = true;
                break;
            default:
                throw new IllegalArgumentException("Input not listed on menu.");
        }
    }
}
