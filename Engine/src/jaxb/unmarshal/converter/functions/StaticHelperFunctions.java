package jaxb.unmarshal.converter.functions;

import objects.world.World;
import properties.property.api.Property;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class StaticHelperFunctions {

    static Object environment(String environmentVarName, World world) {
        Map<String,Property> environmentSet = world.getEnvironment();
        Property environmentVar = environmentSet.get(environmentVarName);
        return environmentVar.getValue();
    }

    static int random(int range){
        Random random = new Random();
        // TODO : check if range is a number.
        return random.nextInt(range + 1);
    }
}
