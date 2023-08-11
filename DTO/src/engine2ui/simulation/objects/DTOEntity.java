package engine2ui.simulation.objects;

import engine2ui.simulation.properties.property.api.DTOProperty;
import engine2ui.simulation.properties.property.impl.NonRangedDTOProperty;
import engine2ui.simulation.properties.property.impl.RangedDTOProperty;

import java.util.List;

public class DTOEntity {
    private String name;
    private int population;
    List<DTOProperty> properties;

    public DTOEntity(String name, int population, List<DTOProperty> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }

    public void addProperty(DTOProperty property)
    {
        properties.add(property);
    }
    public void addRangedProperty(String name, String type, boolean isRandomInit, double from ,double to){
        addProperty(new RangedDTOProperty(name, type, isRandomInit, from, to));
    }

    public void addNonRangedProperty(String name, String type, boolean isRandomInit)
    {
        addProperty(new NonRangedDTOProperty(name, type, isRandomInit));
    }
}
