package validator.ui;

import validator.ui.exceptions.IllegalStringValueException;
import validator.ui.exceptions.OutOfRangeException;

public class ValidatorUI {

    public void isIntegerInRange(int value, int from, int to) throws OutOfRangeException {
        if(value < from || value > to){
            throw new OutOfRangeException();
        }
    }

    public void validateStringValue(String value) throws IllegalStringValueException{
        String pattern = "^[A-Za-z0-9().\\-_,?! ]+$";
        if(!value.matches(pattern)){
            String message = "The string: " + value + "does not match the requested pattern.";
            throw new IllegalStringValueException(message);
        }
    }

}
