package engine2ui.simulation.prview.properties.property.impl;

import engine2ui.simulation.prview.properties.property.api.DTOProperty;
import engine2ui.simulation.prview.properties.property.api.NonRangedProperty;

public class NonRangedDTOProperty extends DTOProperty implements NonRangedProperty {
    public NonRangedDTOProperty(String name, String type, boolean isRandomInit) {
        super(name, type, isRandomInit);
    }
}
