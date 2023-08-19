package engine2ui.simulation.genral.impl.properties.property.impl;

import engine2ui.simulation.genral.impl.properties.property.api.DTOProperty;
import engine2ui.simulation.genral.impl.properties.property.api.NonRangedProperty;

public class NonRangedDTOProperty extends DTOProperty implements NonRangedProperty {
    public NonRangedDTOProperty(String name, String type, boolean isRandomInit, Object value) {
        super(name, type, isRandomInit, value);
    }
}
