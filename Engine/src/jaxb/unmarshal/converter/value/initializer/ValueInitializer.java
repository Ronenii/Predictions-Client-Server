package jaxb.unmarshal.converter.value.initializer;

import jaxb.unmarshal.converter.expression.converter.exception.InvalidBooleanValueException;
import jaxb.unmarshal.converter.expression.converter.exception.InvalidStringValueException;
import jaxb.unmarshal.converter.expression.converter.exception.ValueOutOfRangeException;
import simulation.properties.property.random.value.api.RandomValueGenerator;
import simulation.properties.property.random.value.impl.BoolRndValueGen;
import simulation.properties.property.random.value.impl.DoubleRndValueGen;
import simulation.properties.property.random.value.impl.IntRndValueGen;
import simulation.properties.property.random.value.impl.StringRndValueGen;

/**
 * This class parse strings values or generate random values (according to the 'isRandomInit' flag) for the properties values.
 */
public class ValueInitializer {

    public static int integerInitial(String value, boolean isRandomInit, int from, int to) throws ValueOutOfRangeException {
        int ret;

        if(isRandomInit){
            RandomValueGenerator<Integer> randomValueGenerator = new IntRndValueGen(from, to);
            ret = randomValueGenerator.generateRandomValue();
        }
        else {
            ret = Integer.parseInt(value);
        }

        if (ret < from || ret > to){
            throw new ValueOutOfRangeException();
        }

        return ret;
    }

    public static double doubleInitial(String value, boolean isRandomInit, double from, double to) throws ValueOutOfRangeException {
        double ret;

        if(isRandomInit){
            RandomValueGenerator<Double> randomValueGenerator = new DoubleRndValueGen(from, to);
            ret = randomValueGenerator.generateRandomValue();
        }
        else {
            ret = Double.parseDouble(value);
        }

        if (ret < from || ret > to){
            throw new ValueOutOfRangeException();
        }

        return ret;
    }

    public static boolean booleanInitial(String value, boolean isRandomInit) throws InvalidBooleanValueException {
        boolean ret;

        if(isRandomInit){
            RandomValueGenerator<Boolean> randomValueGenerator = new BoolRndValueGen();
            ret = randomValueGenerator.generateRandomValue();
        }
        else {
            if(value.equals("true")){
                ret = true;
            } else if (value.equals("false")) {
                ret = false;
            }
            else {
                throw new InvalidBooleanValueException();
            }
        }

        return ret;
    }

    public static String stringInitial(String value, boolean isRandomInit) throws InvalidStringValueException {
        String ret, pattern = "^[A-Za-z0-9().\\-_,?! ]+$";

        if(isRandomInit){
            RandomValueGenerator<String> randomValueGenerator = new StringRndValueGen();
            ret = randomValueGenerator.generateRandomValue();
        }
        else {
            if(!value.matches(pattern)){
                throw new InvalidStringValueException();
            }
            ret = value;
        }

        return ret;
    }





}
