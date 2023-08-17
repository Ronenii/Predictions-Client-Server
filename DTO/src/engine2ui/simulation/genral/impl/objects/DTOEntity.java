package engine2ui.simulation.genral.impl.objects;

import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;

public class DTOEntity {
    private final String name;
    private final int startingPopulation;
    private final int endingPopulation;
    private final DTOProperty[] properties;

    public DTOEntity(String name, int StartingPopulation, int endingPopulation, DTOProperty[] properties) {
        this.name = name;
        this.startingPopulation = StartingPopulation;
        this.endingPopulation = endingPopulation;
        this.properties = properties;
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

    @Override
    public String toString() {
        return String.format("Name: %s\n", name) +
                String.format("Population: %s\n", startingPopulation) +
                "Properties: \n";
                //TODO: add printing to properties array
    }
}
