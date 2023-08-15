package validator.ui.exceptions.validator;

import validator.ui.exceptions.IllegalStringValueException;
import validator.ui.exceptions.OutOfRangeException;

public class InputValidator {

    /**
     * Checks if the given Integer value is in the given range.
     * Throws 'OutOfRangeException ' if not.
     */
    public void isIntegerInRange(int value, int from, int to) throws OutOfRangeException {
        if(value < from || value > to){
            throw new OutOfRangeException();
        }
    }

    /**
     * Checks if the given Double value is in the given range.
     * Throws 'OutOfRangeException ' if not.
     */
    public void isDoubleInRange(double value, double from, double to) throws OutOfRangeException {
        if(value < from || value > to){
            throw new OutOfRangeException();
        }
    }


    /**
     * Checks if the given string from the user composed of the pattern regex.
     * Throws 'IllegalStringValueException' if not.
     *
     * @param value the user input
     */
    public void validateStringValue(String value) throws IllegalStringValueException{
        String pattern = "^[A-Za-z0-9().\\-_,?! ]+$";
        if(!value.matches(pattern)){
            String message = "The string: " + value + "does not match the requested pattern.";
            throw new IllegalStringValueException(message);
        }
    }

}
