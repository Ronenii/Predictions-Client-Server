package input;

import manager.validator.exceptions.OutOfRangeException;
import manager.validator.validator.InputValidator;

import java.util.Scanner;

public class Input {

    /**
     * Prompts the user the given prompt and gets the user input as a string.
     * @param prompt The propt the user wants to show when expecting input.
     * @return The user input as a string.
     */
    public static String getInput(String prompt){
        System.out.print(prompt + ": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * @return The user input as a string.
     */
    public static String getInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static int getIntInputForListedItem(String prompt, int listLength){
        int input;
        try {
            input = Integer.parseInt(Input.getInput(String.format("%s (1-%s)",prompt, listLength )));
            InputValidator validator = new InputValidator();

            validator.isIntegerInRange(input, 1, listLength);
        } catch (OutOfRangeException e) {
            throw new IllegalArgumentException("The given input is not in the list.");
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("The given input must be a number.");
        }

        return input;
    }


}
