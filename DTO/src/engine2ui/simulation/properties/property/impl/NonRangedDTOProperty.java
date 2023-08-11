package engine2ui.simulation.properties.property.impl;

import engine2ui.simulation.properties.property.api.DTOProperty;
import engine2ui.simulation.properties.property.api.NonRangedProperty;
import engine2ui.simulation.properties.property.api.RangedProperty;

public class NonRangedDTOProperty extends DTOProperty implements NonRangedProperty {
    public NonRangedDTOProperty(String name, String type, boolean isRandomInit) {
        super(name, type, isRandomInit);
    }
}
