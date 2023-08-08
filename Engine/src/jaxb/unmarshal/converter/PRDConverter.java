package jaxb.unmarshal.converter;

import jaxb.schema.generated.*;
import objects.entity.Entity;
import objects.rule.Rule;
import objects.world.World;
import properties.action.api.Action;
import properties.action.api.ActionType;
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
        Set<Rule> rules = new HashSet<>();
        Set<EndingCondition> endingConditions = new HashSet<>();

        // Iterates over all PRDEnviromentProperties, converts each property and adds it to 'environmentProperties'
        prdWorld.getPRDEvironment().getPRDEnvProperty().forEach(p -> environmentProperties.add(PRDEnvProperty2Property(p)));

        // Iterates over all PRDEntities, converts each entity and adds it to 'entities'
        prdWorld.getPRDEntities().getPRDEntity().forEach(e -> entities.add(PRDEntity2Entity(e)));

        // Iterates over all PRDRules, converts each rule and adds it to 'rules'
        prdWorld.getPRDRules().getPRDRule().forEach(r -> rules.add(PRDRule2Rule(r)));

        return new World(environmentProperties,entities,rules,endingConditions);
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
        prdEntity.getPRDProperties().getPRDProperty().forEach(c -> properties.add(PRDProperty2Property(c)));

        return new Entity(population, name, properties);
    }

    /**
     * Converts the given PRDRule to Rule
     * @param prdRule the given PRDRule generated from reading the XML file
     * @return a Rule representation of PRDRule.
     */
    private static Rule PRDRule2Rule(PRDRule prdRule) {
        String name = prdRule.getName();
        Activation activation = PRDActivation2Activation(prdRule.getPRDActivation());
        Set<Action> actions = null;

        // Iterates over all prdActions inside the prdRule and convert them to action
        prdRule.getPRDActions().getPRDAction().forEach(a -> actions.add(PRDAction2Action(a)));

        return new Rule(name,activation, actions);
    }

    /**
     * Converts the given PRDAction to Action
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return an Action representation of PRDAction.
     */
    private static Action PRDAction2Action(PRDAction prdAction)
    {
        Action ret = null;
        // TODO: Continue this
        switch (ActionType.valueOf(prdAction.getType())) {
            case INCREASE:
                break;
            case DECREASE:
                break;
            case CALCULATION:
                break;
            case CONDITION:
                break;
            case SET:
                break;
            case KILL:
                break;
            case REPLACE:
                break;
            case PROXIMITY:
                break;
        }

        return ret;
    }

    /**
     * Converts the given PRDActivation to Activation
     * @param prdActivation the given PRDAction generated from reading the XML file
     * @return an Activation representation of PRDActivation.
     */
    private static Activation PRDActivation2Activation(PRDActivation prdActivation){
        int ticks = prdActivation.getTicks();
        double probability = prdActivation.getProbability();

        return  new Activation(ticks, probability);
    }
}
