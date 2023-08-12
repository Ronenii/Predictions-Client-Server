package engine2ui.simulation.prview.objects;

import engine2ui.simulation.genral.HasList;
import engine2ui.simulation.prview.properties.property.api.DTOProperty;
import engine2ui.simulation.prview.properties.property.impl.NonRangedDTOProperty;
import engine2ui.simulation.prview.properties.property.impl.RangedDTOProperty;

import java.util.List;

public class DTOEntity implements HasList {
    private final String name;
    private final int population;
    List<DTOProperty> properties;

    public DTOEntity(String name, int population, List<DTOProperty> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }

    public void addProperty(DTOProperty property) {
        properties.add(property);
    }

    public void addRangedProperty(String name, String type, boolean isRandomInit, double from, double to) {
        addProperty(new RangedDTOProperty(name, type, isRandomInit, from, to));
    }

    public void addNonRangedProperty(String name, String type, boolean isRandomInit) {
        addProperty(new NonRangedDTOProperty(name, type, isRandomInit));
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("Name: %s\n", name));
        ret.append(String.format("Population: %s\n", population));
        ret.append("Properties: \n");
        ret.append(formatListToString(properties));

        return ret.toString();
    }
}
