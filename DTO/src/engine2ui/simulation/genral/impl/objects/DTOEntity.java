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

    public DTOEntity(String name, int StartingPopulation, DTOProperty[] properties) {
        this.name = name;
        this.startingPopulation = StartingPopulation;
        this.endingPopulation = 0;
        this.properties = properties;
    }

//    public void addProperty(DTOProperty property) {
//        properties.add(property);
//    }
//
//    public void addRangedProperty(String name, String type, boolean isRandomInit, double from, double to) {
//        addProperty(new RangedDTOProperty(name, type, isRandomInit, from, to));
//    }
//
//    public void addNonRangedProperty(String name, String type, boolean isRandomInit) {
//        addProperty(new NonRangedDTOProperty(name, type, isRandomInit));
//    }

    @Override
    public String toString() {
        return String.format("Name: %s\n", name) +
                String.format("Population: %s\n", startingPopulation) +
                "Properties: \n";
                //TODO: add printing to properties array
    }
}
