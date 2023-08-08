package jaxb.unmarshal.converter.functions;

import objects.world.World;
import properties.property.api.Property;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class StaticHelperFunctions {

    static public Object environment(String environmentVarName, Map<String,Property> environmentVars) {
        Property environmentVar = environmentVars.get(environmentVarName);
        return environmentVar.getValue();
    }

    static public int random(int range){
        Random random = new Random();
        // TODO : check if range is a number.
        return random.nextInt(range + 1);
    }
}
