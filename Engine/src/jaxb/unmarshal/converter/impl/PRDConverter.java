package jaxb.unmarshal.converter.impl;

import jaxb.schema.generated.*;
import objects.entity.Entity;
import objects.rule.Rule;
import objects.world.World;
import properties.action.api.Action;
import properties.activition.Activation;
import properties.ending.conditions.EndingCondition;
import properties.property.api.Property;
import properties.property.api.PropertyType;
import properties.property.impl.BooleanProperty;
import properties.property.impl.DoubleProperty;
import properties.property.impl.IntProperty;
import properties.property.impl.StringProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * A class used to convert the objects generated from the scheme to objects that will be used in the program.
 */
public class PRDConverter {

    public static World PRDWorld2World(PRDWorld prdWorld) {

        Set<Property> environmentProperties = new HashSet<>();
        Set<Entity> entities = new HashSet<>();
        Set<Rule> rule = new HashSet<>();
        Set<EndingCondition> endingConditions = new HashSet<>();

        // Iterates over all PRDEnviromentProperties, converts each property and adds it to 'environmentProperties'
        prdWorld.getPRDEvironment().getPRDEnvProperty().forEach(p -> environmentProperties.add(PRDConverter.PRDEnvProperty2Property(p)));

        // Iterates over all PRDEntities, converts each entity and adds it to 'entities'
        prdWorld.getPRDEntities().getPRDEntity().forEach(e -> entities.add(PRDConverter.PRDEntity2Entity(e)));

        // TODO: Continue this

        World ret = new World(environmentProperties,entities,rule,endingConditions);

        return ret;
    }


    /**
     * Converts the given PRDEnvProperty to Property
     * @param prdEnvProperty the given PRDEnvProperty generated from reading the XML file
     * @return a Property representation of PRDEnvProperty.
     *
     * TODO: Right now I hard coded placeholders for thing such as @value & @is_Random_init in the ctors. We need to make this more elegant.
     */
    private static Property PRDEnvProperty2Property(PRDEnvProperty prdEnvProperty) {
        Property ret = null;
        String name = prdEnvProperty.getPRDName();
        double to = prdEnvProperty.getPRDRange().getTo();
        double from = prdEnvProperty.getPRDRange().getFrom();

        switch (PropertyType.valueOf(prdEnvProperty.getType())) {
            case INT:
                ret = new IntProperty(name, false,0, (int) from, (int) to);
                break;
            case DOUBLE:
                ret = new DoubleProperty(name, false,0.0, from, to);
                break;
            case BOOLEAN:
                ret = new BooleanProperty(name, false, false);
                break;
            case STRING:
                ret = new StringProperty(name, false, "");
                break;
        }

        return ret;
    }


    /**
     * Converts the given PRDProperty to Property
     * @param prdProperty the given prdProperty generated from reading the XML file
     * @return a Property representation of prdProperty.
     */
    private static Property PRDProperty2Property(PRDProperty prdProperty)
    {
        Property ret = null;
        String name = prdProperty.getPRDName();
        double to = prdProperty.getPRDRange().getTo();
        boolean isRandomInit = prdProperty.getPRDValue().isRandomInitialize();
        String value = prdProperty.getPRDValue().getInit();
        double from = prdProperty.getPRDRange().getFrom();

        switch (PropertyType.valueOf(prdProperty.getType())) {
            case INT:
                ret = new IntProperty(name, isRandomInit,Integer.parseInt(value), (int) from, (int) to);
                break;
            case DOUBLE:
                ret = new DoubleProperty(name, isRandomInit,Double.parseDouble(value), from, to);
                break;
            case BOOLEAN:
                ret = new BooleanProperty(name, isRandomInit, Boolean.parseBoolean(value));
                break;
            case STRING:
                ret = new StringProperty(name, isRandomInit, value);
                break;
        }

        return ret;
    }

    /**
     * Converts the given PRDEntity to Entity
     * @param prdEntity the given PRDEntity generated from reading the XML file
     * @return an Entity representation of PRDEntity.
     */
    private static Entity PRDEntity2Entity(PRDEntity prdEntity)
    {
        String name = prdEntity.getName();
        int population = prdEntity.getPRDPopulation();
        Set<Property> properties = new HashSet<>();

        // Iterates over all PRDProperties, converts each property and adds it to 'properties'
        prdEntity.getPRDProperties().getPRDProperty().forEach(c -> properties.add(PRDConverter.PRDProperty2Property(c)));

        Entity ret = new Entity(population, name, properties);

        return  ret;
    }

    /**
     * Converts the given PRDEntity to Entity
     * @param prdRule the given PRDEntity generated from reading the XML file
     * @return an Entity representation of PRDEntity.
     */
    private static Rule PRDRule2Rule(PRDRule prdRule) {
        String name = prdRule.getName();
        Activation activation;
        Set<Action> actions;


        // TODO: Continue this
        Rule ret = new Rule(name,activation, actions);
        return ret;
    }
}
