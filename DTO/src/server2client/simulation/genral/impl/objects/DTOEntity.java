package server2client.simulation.genral.impl.objects;

import server2client.simulation.genral.impl.properties.property.api.DTOProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;

public class DTOEntity implements Serializable {
    private final String name;
    private final int startingPopulation;
    private final int endingPopulation;
    private final DTOProperty[] properties;

    private final DTOEntityInstance[] instances;

    private final SimpleIntegerProperty instanceQuantity;

    public DTOEntity(String name, int StartingPopulation, int endingPopulation, DTOProperty[] properties, DTOEntityInstance[] instances) {
        this.name = name;
        this.startingPopulation = StartingPopulation;
        this.endingPopulation = endingPopulation;
        this.properties = properties;
        this.instances = instances;

        this.instanceQuantity = new SimpleIntegerProperty(startingPopulation);
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

    public SimpleIntegerProperty instanceQuantityProperty() {
        return instanceQuantity;
    }
}
