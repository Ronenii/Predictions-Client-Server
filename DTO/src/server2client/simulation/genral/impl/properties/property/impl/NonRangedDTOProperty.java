package server2client.simulation.genral.impl.properties.property.impl;

import server2client.simulation.genral.impl.properties.property.api.DTOProperty;
import server2client.simulation.genral.impl.properties.property.api.NonRangedProperty;

public class NonRangedDTOProperty extends DTOProperty implements NonRangedProperty {
    public NonRangedDTOProperty(String name, String type, boolean isRandomInit, Object value, int changeTickAmount) {
        super(name, type, isRandomInit, value, changeTickAmount);
    }
}
