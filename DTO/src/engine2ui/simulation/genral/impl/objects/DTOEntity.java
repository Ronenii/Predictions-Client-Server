package engine2ui.simulation.genral.impl.objects;

import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;

import javax.swing.text.html.parser.Entity;

public class DTOEntity {
    private final String name;
    private final int startingPopulation;
    private final int endingPopulation;
    private final DTOProperty[] properties;

    private final DTOEntityInstance[] instances;

    public DTOEntity(String name, int StartingPopulation, int endingPopulation, DTOProperty[] properties, DTOEntityInstance[] instances) {
        this.name = name;
        this.startingPopulation = StartingPopulation;
        this.endingPopulation = endingPopulation;
        this.properties = properties;
        this.instances = instances;
    }

    public String getName() {
        return name;
    }

    public int getStartingPopulation() {
        return startingPopulation;
    }

    public int getEndingPopulation() {
        return endingPopulation;
    }

    public DTOProperty[] getProperties() {
        return properties;
    }

    public DTOEntityInstance[] getInstances() {
        return instances;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\n", name) +
                String.format("Population: %s\n", startingPopulation) +
                "Properties: \n";
        //TODO: add printing to properties array
    }
}
