package jaxb.unmarshal.converter.functions;

import objects.world.World;
import properties.property.api.Property;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * A Static class implements five functions which can be requested as a value for an action object.
 */
public class StaticHelperFunctions {

    /**
     * Receive an environment variable's name and return its value.
     *
     * @param environmentVarName the given environment variable name.
     * @param environmentVars the world's environment variables map.
     * @return the given environment variable's value.
     */
    static public Object environment(String environmentVarName, Map<String,Property> environmentVars) {
        Property environmentVar = environmentVars.get(environmentVarName);
        return environmentVar.getValue();
    }

    /**
     * Receive a number and random an integer between 0 and this number (inclusive).
     *
     * @param range the upper limit of the range.
     * @return the random number.
     */
    static public int random(int range){
        Random random = new Random();
        // TODO : check if range is a number.
        return random.nextInt(range + 1);
    }
}
